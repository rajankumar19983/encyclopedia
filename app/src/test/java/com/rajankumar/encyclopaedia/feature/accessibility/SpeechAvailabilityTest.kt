package com.rajankumar.encyclopaedia.feature.accessibility

import org.junit.Assert.assertEquals
import org.junit.Test

class SpeechAvailabilityTest {
  @Test fun blankContentIsEmpty() = assertEquals(SpeechAvailability.EMPTY, speechAvailability("  "))
  @Test fun contentIsReady() = assertEquals(SpeechAvailability.READY, speechAvailability("CPU scheduling"))
}
