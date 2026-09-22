package com.rajankumar.encyclopaedia.feature.importer

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Test

class OcrLanguageFilterTest {
  @Test
  fun removesLinesContainingDevanagari() {
    val filtered = OcrLanguageFilter.removeDevanagariLines("1. What is RAM?\nयह हिन्दी पंक्ति है\nA. Memory\nB. Storage")
    assertEquals("1. What is RAM?\nA. Memory\nB. Storage", filtered)
    assertFalse(OcrLanguageFilter.containsDevanagari(filtered))
  }
}
