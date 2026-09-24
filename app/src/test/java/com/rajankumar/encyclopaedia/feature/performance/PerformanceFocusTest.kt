package com.rajankumar.encyclopaedia.feature.performance

import org.junit.Assert.assertEquals
import org.junit.Test

class PerformanceFocusTest {
  @Test fun noAttemptsStartsPractice() = assertEquals(PerformanceFocus.START_PRACTICE, PerformanceSummary(0, 0, 0, 10, 0).focus())
  @Test fun lowCoverageTakesPriority() = assertEquals(PerformanceFocus.COVERAGE, PerformanceSummary(10, 5, 2, 10, 60_000).focus())
  @Test fun lowAccuracyFollowsCoverage() = assertEquals(PerformanceFocus.ACCURACY, PerformanceSummary(10, 5, 8, 10, 20_000).focus())
}
