package com.rajankumar.encyclopaedia.feature.accessibility

import org.junit.Assert.assertEquals
import org.junit.Test

class SpeechDurationLabelTest {
  @Test fun labelsEmptyReadingTime() = assertEquals("No reading time", speechDurationLabel(""))
  @Test fun labelsShortReadingTime() = assertEquals("About 1 min", speechDurationLabel("read this content"))
}
