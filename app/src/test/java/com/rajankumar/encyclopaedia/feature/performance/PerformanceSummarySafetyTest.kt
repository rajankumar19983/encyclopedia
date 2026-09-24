package com.rajankumar.encyclopaedia.feature.performance

import org.junit.Assert.assertEquals
import org.junit.Test

class PerformanceSummarySafetyTest {
  @Test fun correctAnswersCannotExceedAttempts() = assertEquals(100, PerformanceSummary(3, 9, 1, 1, 0).accuracy)
  @Test fun uniqueQuestionsCannotExceedTotal() = assertEquals(100, PerformanceSummary(3, 2, 9, 2, 0).coverage)
  @Test fun negativeCountsBecomeEmptyMetrics() { val summary = PerformanceSummary(-3, -1, -2, -4, 0); assertEquals(0, summary.accuracy); assertEquals(0, summary.coverage) }
}
