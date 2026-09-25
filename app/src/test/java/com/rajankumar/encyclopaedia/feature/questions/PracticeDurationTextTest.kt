package com.rajankumar.encyclopaedia.feature.questions

import org.junit.Assert.assertEquals
import org.junit.Test

class PracticeDurationTextTest {
  @Test
  fun formatsSubsecondSecondsMinutesAndHours() {
    assertEquals("0s", formatPracticeDuration(0))
    assertEquals("<1s", formatPracticeDuration(999))
    assertEquals("12s", formatPracticeDuration(12_999))
    assertEquals("1m 1s", formatPracticeDuration(61_000))
    assertEquals("1h 2m", formatPracticeDuration(3_720_000))
  }

  @Test
  fun clampsNegativeDurations() {
    assertEquals("0s", formatPracticeDuration(-1_000))
  }
}
