package com.rajankumar.encyclopaedia.feature.importer

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class OcrAnswerNormalizationTest {
  @Test fun normalizesLettersAndNumbers() {
    assertEquals("C", normalizeOcrAnswerToken("c"))
    assertEquals("E", normalizeOcrAnswerToken("5"))
    assertEquals("B", normalizeOcrAnswerToken("(B)"))
  }

  @Test fun rejectsOutOfRangeTokens() {
    assertNull(normalizeOcrAnswerToken("27"))
    assertNull(normalizeOcrAnswerToken("AB"))
  }
}
