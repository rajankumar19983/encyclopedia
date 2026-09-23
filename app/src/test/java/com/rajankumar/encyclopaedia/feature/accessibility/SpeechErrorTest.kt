package com.rajankumar.encyclopaedia.feature.accessibility

import org.junit.Assert.assertTrue
import org.junit.Test

class SpeechErrorTest {
  @Test fun errorsHaveUsefulMessages() {
    SpeechError.entries.forEach { assertTrue(it.message().isNotBlank()) }
  }
}