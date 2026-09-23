package com.rajankumar.encyclopaedia.feature.performance

import org.junit.Assert.assertEquals
import org.junit.Test

class PerformanceConfidenceTest {
  @Test fun confidenceGrowsWithEvidence() {
    assertEquals(PerformanceConfidence.NONE, performanceConfidence(0))
    assertEquals(PerformanceConfidence.LOW, performanceConfidence(5))
    assertEquals(PerformanceConfidence.MODERATE, performanceConfidence(20))
    assertEquals(PerformanceConfidence.HIGH, performanceConfidence(40))
  }
}
