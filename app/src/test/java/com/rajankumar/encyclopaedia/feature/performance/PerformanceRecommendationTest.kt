package com.rajankumar.encyclopaedia.feature.performance

import org.junit.Assert.assertEquals
import org.junit.Test

class PerformanceRecommendationTest {
  @Test
  fun emptyPerformanceRecommendsStartingPractice() {
    val data = PerformanceData(
      summary = PerformanceSummary(0, 0, 0, 10, 0L),
      weakQuestions = emptyList(),
      trend = AccuracyTrend(0, 0),
      speedTrend = SpeedTrend(0L, 0L)
    )

    assertEquals("Start with a short practice session.", data.recommendation())
  }

  @Test
  fun lowCoverageIsPrioritizedBeforeLowAccuracy() {
    val data = PerformanceData(
      summary = PerformanceSummary(10, 5, 4, 10, 1_000L),
      weakQuestions = emptyList(),
      trend = AccuracyTrend(50, 50),
      speedTrend = SpeedTrend(1_000L, 1_000L)
    )

    assertEquals("Practise more unseen questions to broaden coverage.", data.recommendation())
  }
}
