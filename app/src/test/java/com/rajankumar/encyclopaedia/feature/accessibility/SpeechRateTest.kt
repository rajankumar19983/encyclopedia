package com.rajankumar.encyclopaedia.feature.accessibility

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class SpeechRateTest {
  @Test
  fun presetsRemainOrderedFromSlowToFast() {
    assertTrue(SpeechRate.SLOW.value < SpeechRate.NORMAL.value)
    assertTrue(SpeechRate.NORMAL.value < SpeechRate.FAST.value)
  }

  @Test
  fun normalRateUsesAndroidDefaultMultiplier() {
    assertEquals(1.0f, SpeechRate.NORMAL.value)
  }
}
