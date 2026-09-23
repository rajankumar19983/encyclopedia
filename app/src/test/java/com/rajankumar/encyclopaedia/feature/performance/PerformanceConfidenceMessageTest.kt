package com.rajankumar.encyclopaedia.feature.performance

import org.junit.Assert.assertTrue
import org.junit.Test

class PerformanceConfidenceMessageTest {
  @Test fun lowConfidenceIsClearlyQualified() = assertTrue(PerformanceConfidence.LOW.message().contains("limited practice history"))
  @Test fun mediumConfidenceEncouragesMoreEvidence() = assertTrue(PerformanceConfidence.MEDIUM.message().contains("more practice"))
  @Test fun highConfidenceDescribesEstablishedHistory() = assertTrue(PerformanceConfidence.HIGH.message().contains("established practice history"))
}
