package com.rajankumar.encyclopaedia.feature.accessibility

import org.junit.Assert.assertEquals
import org.junit.Test

class SpeechWordCountTest {
  @Test fun countsSanitizedWords() = assertEquals(3, speechWordCount(" operating   system concepts "))
  @Test fun labelsEmptyText() = assertEquals("No readable words", speechWordCountLabel(""))
}
