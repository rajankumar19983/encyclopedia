package com.rajankumar.encyclopaedia.feature.performance

import org.junit.Assert.assertEquals
import org.junit.Test

class PerformanceEvidenceTest {
  @Test fun combinesAccuracyCoverageAndConfidence() {
    val evidence = performanceEvidence(20, 10, 12, 9)
    assertEquals(75, evidence.accuracy.percent)
    assertEquals(50, evidence.coverage.percent)
    assertEquals(PerformanceConfidence.LOW, evidence.confidence)
  }
}
