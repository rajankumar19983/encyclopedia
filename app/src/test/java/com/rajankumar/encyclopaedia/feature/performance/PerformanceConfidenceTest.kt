package com.rajankumar.encyclopaedia.feature.performance

import org.junit.Assert.assertEquals
import org.junit.Test

class PerformanceConfidenceTest {
  @Test fun confidenceGrowsWithEvidence() {
    assertEquals(PerformanceConfidence.LOW, performanceConfidence(0))
    assertEquals(PerformanceConfidence.LOW, performanceConfidence(5))
    assertEquals(PerformanceConfidence.MEDIUM, performanceConfidence(20))
    assertEquals(PerformanceConfidence.MEDIUM, performanceConfidence(40))
    assertEquals(PerformanceConfidence.HIGH, performanceConfidence(100))
  }
}
