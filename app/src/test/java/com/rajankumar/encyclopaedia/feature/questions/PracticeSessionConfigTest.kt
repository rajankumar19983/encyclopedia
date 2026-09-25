package com.rajankumar.encyclopaedia.feature.questions

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class PracticeSessionConfigTest {
  @Test
  fun questionBankPresetRoundTripsIntoPracticeConfig() {
    val filters = QuestionBankFilterState(
      query = "register",
      source = "AI",
      difficulty = "HARD",
      topicId = "cpu",
    )

    val config = filters.toPracticeSessionConfig(
      mode = PracticeMode.NEW,
      questionCount = 30,
    )

    assertEquals(PracticeMode.NEW, config.mode)
    assertEquals(30, config.safeQuestionCount)
    assertEquals(filters, config.toQuestionBankFilterState())
    assertTrue(config.hasFilters)
  }

  @Test
  fun emptyConfigHasNoFiltersAndClampsQuestionCount() {
    val config = PracticeSessionConfig(questionCount = 500)
    assertFalse(config.hasFilters)
    assertEquals(PracticeConstants.maxSessionQuestions, config.safeQuestionCount)
  }

  @Test
  fun filteredEmptyMessageExplainsTheSelectedSubset() {
    val config = PracticeSessionConfig(source = "AI")
    assertTrue(config.emptyMessage().contains("selected filters"))
  }
}
