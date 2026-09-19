package com.rajankumar.encyclopaedia.feature.performance

import org.junit.Assert.assertEquals
import org.junit.Test

class PerformanceBandsTest {
  @Test
  fun accuracyBandsHonorBoundaries() {
    assertEquals(AccuracyBand.NEEDS_WORK, accuracyBand(49))
    assertEquals(AccuracyBand.DEVELOPING, accuracyBand(50))
    assertEquals(AccuracyBand.GOOD, accuracyBand(70))
    assertEquals(AccuracyBand.EXCELLENT, accuracyBand(85))
  }

  @Test
  fun coverageBandsHonorBoundaries() {
    assertEquals(CoverageBand.STARTING, coverageBand(24))
    assertEquals(CoverageBand.PARTIAL, coverageBand(25))
    assertEquals(CoverageBand.BROAD, coverageBand(60))
    assertEquals(CoverageBand.COMPLETE, coverageBand(100))
  }

  @Test
  fun confidenceUsesAttemptThresholds() {
    assertEquals(PerformanceConfidence.LOW, performanceConfidence(19))
    assertEquals(PerformanceConfidence.MEDIUM, performanceConfidence(20))
    assertEquals(PerformanceConfidence.HIGH, performanceConfidence(100))
  }
}
