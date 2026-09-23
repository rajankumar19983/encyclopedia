package com.rajankumar.encyclopaedia.feature.performance

import org.junit.Assert.assertTrue
import org.junit.Test

class PerformanceAccessibilityTest {
  @Test fun descriptionReadsPercentagesAndAction() {
    val text = performanceEvidence(20,10,12,9).accessibilityDescription()
    assertTrue(text.contains("75 percent accuracy"))
    assertTrue(text.contains("50 percent question coverage"))
    assertTrue(text.contains("Recommended next step"))
  }
}
