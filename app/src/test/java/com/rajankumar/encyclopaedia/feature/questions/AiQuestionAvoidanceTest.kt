package com.rajankumar.encyclopaedia.feature.questions

import com.rajankumar.encyclopaedia.data.local.QuestionEntity
import com.rajankumar.encyclopaedia.data.local.QuestionTopicEntity
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class AiQuestionAvoidanceTest {
  private fun question(id: String, text: String, createdAt: Long) = QuestionEntity(
    id = id,
    questionText = text,
    options = "A\nB\nC\nD",
    correctAnswer = "A",
    createdAt = createdAt,
    updatedAt = createdAt,
  )

  @Test
  fun selectedTopicQuestionsArePrioritizedBeforeGlobalQuestions() {
    val questions = listOf(
      question("global-new", "Global newest", 30),
      question("topic-old", "Selected topic question", 10),
      question("global-old", "Global older", 20),
    )
    val links = listOf(QuestionTopicEntity("topic-old", "cpu"))

    val result = selectAiQuestionAvoidanceTexts(questions, links, "cpu")

    assertEquals("Selected topic question", result.first())
    assertEquals(listOf("Global newest", "Global older"), result.drop(1))
  }

  @Test
  fun normalizedDuplicateStemsAppearOnlyOnce() {
    val result = selectAiQuestionAvoidanceTexts(
      questions = listOf(
        question("one", "What is RAM?", 20),
        question("two", "WHAT is RAM ?", 10),
      ),
      topicLinks = emptyList(),
      topicId = null,
    )

    assertEquals(1, result.size)
  }

  @Test
  fun avoidanceListIsBoundedByItemCount() {
    val questions = (1..100).map { index -> question("q$index", "Question $index", index.toLong()) }

    val result = selectAiQuestionAvoidanceTexts(questions, emptyList(), null)

    assertEquals(AI_QUESTION_AVOID_MAX_ITEMS, result.size)
  }

  @Test
  fun formattedAvoidanceTextIsStrictlyBounded() {
    val text = buildAiQuestionAvoidanceText(
      (1..AI_QUESTION_AVOID_MAX_ITEMS).map { index -> "Question $index ${"x".repeat(500)}" },
    )

    assertTrue(text!!.length <= AI_QUESTION_AVOID_MAX_CHARS)
    assertFalse(text.contains("Question 40 ${"x".repeat(500)}"))
  }
}
