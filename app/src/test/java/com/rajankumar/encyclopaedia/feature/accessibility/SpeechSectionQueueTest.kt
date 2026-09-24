package com.rajankumar.encyclopaedia.feature.accessibility

import org.junit.Assert.assertEquals
import org.junit.Test

class SpeechSectionQueueTest {
  @Test fun dropsEmptySections() { val queue = listOf(SpeechSection("Question", "Text"), SpeechSection("", "")).speechQueue(); assertEquals(listOf("Question. Text"), queue) }
}
