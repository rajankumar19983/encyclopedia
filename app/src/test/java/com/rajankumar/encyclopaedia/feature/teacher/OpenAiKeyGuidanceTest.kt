package com.rajankumar.encyclopaedia.feature.teacher

import org.junit.Assert.assertTrue
import org.junit.Test

class OpenAiKeyGuidanceTest {
  @Test fun guidanceExplainsStorageRiskAndRotation() {
    val guidance = openAiKeyGuidance()
    assertTrue(guidance.localStorageNotice.contains("encrypted"))
    assertTrue(guidance.riskNotice.contains("compromised device"))
    assertTrue(guidance.rotationNotice.contains("Rotate"))
  }
}
