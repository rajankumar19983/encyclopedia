package com.rajankumar.encyclopaedia.feature.accessibility

import org.junit.Assert.assertEquals
import org.junit.Test

class SpeechDurationTest {
  @Test fun emptyTextHasZeroDuration() = assertEquals(0, estimatedSpeechMinutes(""))
  @Test fun shortTextRoundsToOneMinute() = assertEquals(1, estimatedSpeechMinutes("one two three"))
}
