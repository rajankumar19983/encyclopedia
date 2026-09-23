package com.rajankumar.encyclopaedia.feature.importer

import org.junit.Assert.assertEquals
import org.junit.Test

class OcrOptionLabelsTest {
  @Test fun supportsCommonVariableOptionCounts() {
    assertEquals("A", ocrOptionLabel(0))
    assertEquals("F", ocrOptionLabel(5))
    assertEquals("G", ocrOptionLabel(6))
  }

  @Test fun continuesBeyondSingleLetters() {
    assertEquals("Z", ocrOptionLabel(25))
    assertEquals("AA", ocrOptionLabel(26))
    assertEquals("AB", ocrOptionLabel(27))
  }
}
