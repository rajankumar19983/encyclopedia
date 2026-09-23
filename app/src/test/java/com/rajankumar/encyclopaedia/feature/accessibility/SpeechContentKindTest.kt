package com.rajankumar.encyclopaedia.feature.accessibility

import org.junit.Assert.assertEquals
import org.junit.Test

class SpeechContentKindTest {
  @Test fun labelsAreReadable() {
    assertEquals("Lesson", SpeechContentKind.LESSON.label())
    assertEquals("Explanation", SpeechContentKind.EXPLANATION.label())
  }
}