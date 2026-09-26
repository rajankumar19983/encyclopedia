package com.rajankumar.encyclopaedia.feature.questions

import com.rajankumar.encyclopaedia.data.local.KnowledgeNodeEntity
import com.rajankumar.encyclopaedia.data.local.QuestionEntity
import com.rajankumar.encyclopaedia.data.local.QuestionTopicEntity
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class PracticeSessionBreakdownTest {
  @Test
  fun buildsDifficultySourceTopicAndSlowestBreakdowns() {
    val reviews = listOf(
      review(question("q1", "CPU one", "AI", "HARD"), correct = true, timeMs = 10_000),
      review(question("q2", "CPU two", "AI", "hard"), correct = false, timeMs = 30_000),
      review(question("q3", "Memory", "USER", "EASY"), correct = true, timeMs = 20_000),
      review(question("q4", "Unlinked", "PDF", "MEDIUM"), correct = false, timeMs = 40_000),
    )
    val topics = listOf(topic("cpu", "CPU"), topic("memory", "Memory"))
    val links = listOf(
      QuestionTopicEntity("q1", "cpu"),
      QuestionTopicEntity("q2", "cpu"),
      QuestionTopicEntity("q3", "memory"),
    )

    val result = buildPracticeSessionBreakdown(reviews, topics, links)

    val hard = result.byDifficulty.single { it.key == "HARD" }
    assertEquals(2, hard.total)
    assertEquals(1, hard.correct)
    assertEquals(50, hard.accuracyPercent)
    assertEquals(20_000L, hard.averageTimeMs)

    val ai = result.bySource.single { it.key == "AI" }
    assertEquals(2, ai.total)
    assertEquals(50, ai.accuracyPercent)

    val cpu = result.byTopic.single { it.label == "CPU" }
    assertEquals(2, cpu.total)
    assertEquals(1, cpu.mistakes)
    assertEquals("cpu", cpu.referenceId)
    assertEquals("Unlinked", result.byTopic.first().label)

    assertEquals(listOf("q4", "q2", "q3"), result.slowestQuestions.map { it.questionId })
    assertEquals(PracticeFocusDimension.TOPIC, result.focusInsight?.dimension)
    assertEquals("CPU", result.focusInsight?.row?.label)
    assertEquals("cpu", result.focusInsight?.row?.referenceId)
  }

  @Test
  fun combinesMultipleTopicLinksWithoutDoubleCountingOrAmbiguousAction() {
    val reviews = listOf(review(question("q1", "Question", "USER", "MEDIUM"), true, 5_000))
    val topics = listOf(topic("cpu", "CPU"), topic("registers", "Registers"))
    val links = listOf(
      QuestionTopicEntity("q1", "registers"),
      QuestionTopicEntity("q1", "cpu"),
    )

    val result = buildPracticeSessionBreakdown(reviews, topics, links)

    assertEquals(1, result.byTopic.size)
    assertEquals("CPU + Registers", result.byTopic.single().label)
    assertEquals(1, result.byTopic.single().total)
    assertNull(result.byTopic.single().referenceId)
    assertNull(result.focusInsight)
  }

  @Test
  fun duplicateTopicNamesStayDistinctByStableId() {
    val reviews = listOf(
      review(question("q1", "First CPU", "USER", "MEDIUM"), false, 5_000),
      review(question("q2", "Second CPU", "USER", "MEDIUM"), false, 6_000),
      review(question("q3", "First CPU again", "USER", "MEDIUM"), true, 4_000),
    )
    val topics = listOf(topic("cpu-a", "CPU"), topic("cpu-b", "CPU"))
    val links = listOf(
      QuestionTopicEntity("q1", "cpu-a"),
      QuestionTopicEntity("q3", "cpu-a"),
      QuestionTopicEntity("q2", "cpu-b"),
    )

    val result = buildPracticeSessionBreakdown(reviews, topics, links)

    val cpuRows = result.byTopic.filter { it.label == "CPU" }
    assertEquals(2, cpuRows.size)
    assertEquals(setOf("cpu-a", "cpu-b"), cpuRows.mapNotNull { it.referenceId }.toSet())
    assertEquals("cpu-a", result.focusInsight?.row?.referenceId)
  }

  @Test
  fun multiLinkedWeakRowsFallBackToDifficultyFocus() {
    val reviews = listOf(
      review(question("q1", "One", "USER", "HARD"), false, 5_000),
      review(question("q2", "Two", "USER", "HARD"), false, 6_000),
    )
    val topics = listOf(topic("cpu", "CPU"), topic("registers", "Registers"))
    val links = listOf(
      QuestionTopicEntity("q1", "cpu"),
      QuestionTopicEntity("q1", "registers"),
      QuestionTopicEntity("q2", "cpu"),
      QuestionTopicEntity("q2", "registers"),
    )

    val result = buildPracticeSessionBreakdown(reviews, topics, links)

    assertNull(result.byTopic.single().referenceId)
    assertEquals(PracticeFocusDimension.DIFFICULTY, result.focusInsight?.dimension)
    assertEquals("Hard", result.focusInsight?.row?.label)
  }

  @Test
  fun negativeTimesAreClampedAndEmptySessionHasNoFocus() {
    val one = buildPracticeSessionBreakdown(
      listOf(review(question("q1", "Question", "USER", "MEDIUM"), false, -500)),
    )
    assertEquals(0L, one.byDifficulty.single().totalTimeMs)
    assertEquals(0L, one.slowestQuestions.single().timeTakenMs)

    val empty = buildPracticeSessionBreakdown(emptyList())
    assertEquals(emptyList<PracticeBreakdownRow>(), empty.byDifficulty)
    assertEquals(emptyList<PracticeSlowQuestion>(), empty.slowestQuestions)
    assertNull(empty.focusInsight)
  }

  private fun question(id: String, text: String, source: String, difficulty: String) = QuestionEntity(
    id = id,
    questionText = text,
    options = "One\nTwo\nThree\nFour",
    correctAnswer = "A",
    source = source,
    difficulty = difficulty,
  )

  private fun topic(id: String, name: String) = KnowledgeNodeEntity(
    id = id,
    parentId = null,
    name = name,
  )

  private fun review(question: QuestionEntity, correct: Boolean, timeMs: Long) = PracticeAnswerReview(
    question = question,
    selectedAnswer = if (correct) "A" else "B",
    wasCorrect = correct,
    timeTakenMs = timeMs,
  )
}
