package com.rajankumar.encyclopaedia.feature.questions

import com.rajankumar.encyclopaedia.data.local.QuestionEntity
import com.rajankumar.encyclopaedia.data.local.QuestionTopicEntity
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class QuestionBankFilterTest {
  private fun question(
    id: String,
    text: String,
    source: String,
    difficulty: String,
  ) = QuestionEntity(
    id = id,
    questionText = text,
    options = "A\nB\nC\nD",
    correctAnswer = "A",
    explanation = "Explanation for $text",
    source = source,
    difficulty = difficulty,
  )

  @Test
  fun combinesSearchSourceDifficultyAndTopicFilters() {
    val questions = listOf(
      question("q1", "Which CPU register stores the next instruction address?", "AI", "hard"),
      question("q2", "Which CPU register stores an instruction?", "AI", "HARD"),
      question("q3", "Which CPU register stores the next instruction address?", "USER", "HARD"),
    )
    val links = listOf(
      QuestionTopicEntity("q1", "cpu"),
      QuestionTopicEntity("q2", "registers"),
      QuestionTopicEntity("q3", "cpu"),
    )

    val result = questions.applyQuestionBankFilters(
      QuestionBankFilterState(
        query = "next instruction",
        source = " ai ",
        difficulty = "HARD",
        topicId = "cpu",
      ),
      links,
    )

    assertEquals(listOf("q1"), result.map { it.id })
  }

  @Test
  fun sourceAndDifficultyMatchingAreNormalized() {
    val questions = listOf(question("q1", "Question", " ai ", "hard"))
    val result = questions.applyQuestionBankFilters(
      QuestionBankFilterState(source = "AI", difficulty = "HARD"),
    )
    assertEquals(listOf("q1"), result.map { it.id })
  }

  @Test
  fun unknownStoredSourcesRemainAvailableAsFilterChoices() {
    val sources = availableQuestionSources(
      listOf(question("q1", "Question", "CUSTOM_IMPORT", "MEDIUM")),
    )
    assertTrue("AI" in sources)
    assertTrue("CUSTOM_IMPORT" in sources)
  }

  @Test
  fun filterStateReportsWhetherAnythingIsActive() {
    assertFalse(QuestionBankFilterState().hasActiveFilters)
    assertTrue(QuestionBankFilterState(topicId = "cpu").hasActiveFilters)
    assertTrue(QuestionBankFilterState(query = "ram").hasActiveFilters)
  }
}
