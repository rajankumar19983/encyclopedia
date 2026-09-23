package com.rajankumar.encyclopaedia.feature.accessibility

import org.junit.Assert.assertEquals
import org.junit.Test

class SpeechActionLabelTest {
  @Test fun labelsDescribeAction() {
    assertEquals("Read question aloud", speechActionLabel(SpeechContentKind.QUESTION, false))
    assertEquals("Stop reading lesson", speechActionLabel(SpeechContentKind.LESSON, true))
  }
}