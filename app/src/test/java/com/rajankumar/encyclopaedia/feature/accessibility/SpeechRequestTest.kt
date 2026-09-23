package com.rajankumar.encyclopaedia.feature.accessibility

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class SpeechRequestTest {
  @Test fun normalizesSpeakableText() {
    val request = SpeechRequest(" Question   text ", SpeechContentKind.QUESTION)
    assertTrue(request.speakable)
    assertEquals("Question text", request.normalizedText)
  }
  @Test fun blankRequestIsNotSpeakable() = assertFalse(SpeechRequest(" ").speakable)
}