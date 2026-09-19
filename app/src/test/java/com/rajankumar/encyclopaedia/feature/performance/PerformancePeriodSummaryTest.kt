package com.rajankumar.encyclopaedia.feature.performance

import org.junit.Assert.assertEquals
import org.junit.Test

class PerformancePeriodSummaryTest {
  @Test
  fun periodSummaryAccuracyHandlesCounts() {
    val summary = PerformancePeriodSummary(PerformancePeriod.WEEK, attempts = 4, correct = 3)

    assertEquals(75, summary.accuracy)
  }

  @Test
  fun emptyPeriodSummaryHasZeroAccuracy() {
    val summary = PerformancePeriodSummary(PerformancePeriod.MONTH, attempts = 0, correct = 0)

    assertEquals(0, summary.accuracy)
  }
}
