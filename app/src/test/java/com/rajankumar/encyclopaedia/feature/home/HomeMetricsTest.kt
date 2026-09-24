package com.rajankumar.encyclopaedia.feature.home

import org.junit.Assert.*
import org.junit.Test

class HomeMetricsTest {
  @Test fun calculatesAccuracyCoverageAndRemaining() = assertEquals(HomeMetrics(75, 50, 10), calculateHomeMetrics(20, 8, 6, 10))
  @Test fun emptyHistoryProducesZeroPercentages() = assertEquals(HomeMetrics(0, 0, 0), calculateHomeMetrics(0, 0, 0, 0))
  @Test fun metricsAreBounded() = assertEquals(HomeMetrics(100, 100, 0), calculateHomeMetrics(4, 2, 3, 7))
  @Test fun fullCoverageMarksPracticeComplete() = assertTrue(calculateHomeMetrics(4, 4, 4, 4).practiceComplete)
}
