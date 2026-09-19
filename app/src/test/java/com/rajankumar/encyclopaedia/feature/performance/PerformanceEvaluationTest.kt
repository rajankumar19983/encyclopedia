package com.rajankumar.encyclopaedia.feature.performance

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class PerformanceEvaluationTest {
  @Test
  fun goalsReportOnlyRemainingProgress() {
    val goal = PerformanceGoal(targetAccuracy = 80, targetCoverage = 90)

    assertEquals(15, goal.accuracyRemaining(65))
    assertEquals(0, goal.accuracyRemaining(95))
    assertEquals(40, goal.coverageRemaining(50))
  }

  @Test
  fun dataQualityRequiresTwentyAttemptsForTrend() {
    val limited = PerformanceSummary(19, 10, 10, 20, 1_000L).dataQuality()
    val enough = PerformanceSummary(20, 10, 10, 20, 1_000L).dataQuality()

    assertFalse(limited.enoughForTrend)
    assertTrue(enough.enoughForTrend)
    assertEquals(PerformanceConfidence.LOW, limited.confidence)
    assertEquals(PerformanceConfidence.MEDIUM, enough.confidence)
  }
}
