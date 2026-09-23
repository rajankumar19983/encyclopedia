package com.rajankumar.encyclopaedia.feature.accessibility

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class SpeechStateTest {
  @Test fun preparingAndSpeakingAreBusy() {
    assertTrue(SpeechState.PREPARING.isBusy())
    assertTrue(SpeechState.SPEAKING.isBusy())
    assertFalse(SpeechState.IDLE.isBusy())
  }
}