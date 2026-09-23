package com.rajankumar.encyclopaedia.feature.importer

import org.junit.Assert.assertEquals
import org.junit.Test

class OcrRemovedLinePreviewTest {
  @Test fun limitsAndExplainsRemovedLines() {
    val result = OcrSanitizationResult("Question", listOf(
      OcrLineQuality("हिंदी", false, "contains Devanagari"),
      OcrLineQuality("@@@", false, "mostly OCR noise"),
    ))
    val previews = result.removedLinePreviews(1)
    assertEquals(1, previews.size)
    assertEquals("contains Devanagari", previews.single().reason)
  }
}
