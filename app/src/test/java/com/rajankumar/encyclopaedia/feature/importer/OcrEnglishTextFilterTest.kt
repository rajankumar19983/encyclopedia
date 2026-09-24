package com.rajankumar.encyclopaedia.feature.importer

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class OcrEnglishTextFilterTest {
  @Test fun removesDevanagariButKeepsEnglishAndStructure() {
    val filtered = OcrEnglishTextFilter.filter("1. What is RAM? रैम क्या है?\nA. Memory मेमोरी\nB. CPU")
    assertFalse(OcrEnglishTextFilter.containsDevanagari(filtered))
    assertTrue(filtered.contains("1. What is RAM?"))
    assertTrue(filtered.contains("A. Memory"))
    assertTrue(filtered.contains("B. CPU"))
  }
}
