package com.rajankumar.encyclopaedia.feature.accessibility

import org.junit.Assert.assertEquals
import org.junit.Test

class SpeechTextSanitizerTest {
  @Test fun collapsesWhitespace() = assertEquals("Hello world", sanitizeSpeechText("  Hello\n  world  "))
}