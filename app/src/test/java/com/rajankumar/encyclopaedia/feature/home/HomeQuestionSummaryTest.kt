package com.rajankumar.encyclopaedia.feature.home

import org.junit.Assert.assertEquals
import org.junit.Test

class HomeQuestionSummaryTest {
  @Test fun reportsRemainingQuestions() = assertEquals(70, HomeQuestionSummary(100, 30).remaining)
  @Test fun attemptedCountCannotMakeRemainingNegative() = assertEquals(0, HomeQuestionSummary(10, 20).remaining)
}
