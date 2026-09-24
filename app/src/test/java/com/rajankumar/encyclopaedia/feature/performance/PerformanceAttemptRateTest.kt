package com.rajankumar.encyclopaedia.feature.performance

import org.junit.Assert.assertEquals
import org.junit.Test

class PerformanceAttemptRateTest {
  @Test fun calculatesAttemptsPerUniqueQuestion() = assertEquals(2.5, PerformanceSummary(5, 3, 2, 10, 0).attemptsPerQuestion(), 0.0)
  @Test fun emptyQuestionSetHasZeroRate() = assertEquals(0.0, PerformanceSummary(0, 0, 0, 0, 0).attemptsPerQuestion(), 0.0)
}
