package com.rajankumar.encyclopaedia.feature.importer

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Test

class OcrEnglishOnlyRequirementTest {
  @Test fun removesDevanagariAndKeepsEnglishMcqText() {
    val filtered = OcrEnglishTextFilter.filter("1. CPU क्या है?\nA. Processor प्रोसेसर\nB. Memory")
    assertFalse(OcrEnglishTextFilter.containsDevanagari(filtered))
    assertEquals("1. CPU ?\nA. Processor\nB. Memory", filtered)
  }
}
