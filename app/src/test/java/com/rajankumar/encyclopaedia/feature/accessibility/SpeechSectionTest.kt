package com.rajankumar.encyclopaedia.feature.accessibility

import org.junit.Assert.assertEquals
import org.junit.Test

class SpeechSectionTest {
  @Test fun combinesHeadingAndText() = assertEquals("Question. What is paging?", SpeechSection(" Question ", " What is paging? ").speechText())
  @Test fun blankHeadingUsesTextOnly() = assertEquals("Answer", SpeechSection("", "Answer").speechText())
}
