package com.rajankumar.encyclopaedia.feature.performance

import org.junit.Assert.assertEquals
import org.junit.Test

class PerformanceTimingTest {
  @Test fun noTimingDataIsReadable() = assertEquals("No timing data", PerformanceSummary(0, 0, 0, 0, 0).averageTimeText())
  @Test fun secondsAreReadable() = assertEquals("25s average", PerformanceSummary(1, 1, 1, 1, 25_000).averageTimeText())
  @Test fun minutesAreReadable() = assertEquals("1m 15s average", PerformanceSummary(1, 1, 1, 1, 75_000).averageTimeText())
}
