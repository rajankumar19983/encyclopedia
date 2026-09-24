package com.rajankumar.encyclopaedia.feature.accessibility

import org.junit.Assert.assertEquals
import org.junit.Test

class SpeechProgressLabelTest {
  @Test fun emptyQueueIsNotReading() = assertEquals("Not reading", speechSectionProgressLabel(0, 0))
  @Test fun formatsCurrentSection() = assertEquals("Reading section 2 of 4", speechSectionProgressLabel(1, 4))
}
