package com.rajankumar.encyclopaedia.feature.accessibility

import org.junit.Assert.assertEquals
import org.junit.Test

class SpeechRateBoundsTest {
  @Test fun rateIsClampedToSupportedRange() {
    assertEquals(0.5f, boundedSpeechRate(0.1f))
    assertEquals(1.25f, boundedSpeechRate(1.25f))
    assertEquals(2f, boundedSpeechRate(4f))
  }
}