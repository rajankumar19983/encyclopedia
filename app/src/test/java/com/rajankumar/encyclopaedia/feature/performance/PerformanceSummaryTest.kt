package com.rajankumar.encyclopaedia.feature.performance

import org.junit.Assert.assertEquals
import org.junit.Test

class PerformanceSummaryTest {
  @Test
  fun accuracyAndCoverageUseAttemptAndQuestionCounts() {
    val summary = PerformanceSummary(
      attempts = 4,
      correct = 3,
      uniqueQuestions = 2,
      totalQuestions = 5,
      averageTimeMs = 1_500L
    )

    assertEquals(75, summary.accuracy)
    assertEquals(40, summary.coverage)
  }

  @Test
  fun emptySummaryReturnsZeroPercentages() {
    val summary = PerformanceSummary(0, 0, 0, 0, 0L)

    assertEquals(0, summary.accuracy)
    assertEquals(0, summary.coverage)
  }
}
