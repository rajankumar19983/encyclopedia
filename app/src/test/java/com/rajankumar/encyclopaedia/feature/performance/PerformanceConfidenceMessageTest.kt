package com.rajankumar.encyclopaedia.feature.performance

import org.junit.Assert.assertTrue
import org.junit.Test

class PerformanceConfidenceMessageTest {
  @Test fun lowConfidenceIsClearlyQualified() = assertTrue(PerformanceConfidence.LOW.message().contains("small number"))
  @Test fun noEvidencePromptsPractice() = assertTrue(PerformanceConfidence.NONE.message().contains("Complete a session"))
}
