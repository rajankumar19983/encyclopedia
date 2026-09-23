package com.rajankumar.encyclopaedia.feature.importer

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class OcrSanitizationSummaryTest {
  @Test fun cleanInputNeedsNoSummary() = assertNull(OcrSanitizationResult("Question", emptyList()).summary().message())

  @Test fun reportsExcludedLineCategories() {
    val result = OcrSanitizationResult("Question", listOf(OcrLineQuality("हिंदी", false, "contains Devanagari"), OcrLineQuality("@@@", false, "mostly OCR noise")))
    assertEquals("Excluded 2 suspect OCR lines from parsing • 1 Devanagari • 1 noise.", result.summary().message())
  }
}
