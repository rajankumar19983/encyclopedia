package com.rajankumar.encyclopaedia.feature.importer

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class OcrExtractionNoticeTest {
  @Test fun failedPagesAreNamedForReview() {
    val result = OcrExtractionResult(OcrSourceKind.PDF, listOf(
      OcrPageText(1, "ok"), OcrPageText(2, "", "bad"), OcrPageText(5, "", "bad")
    ))
    val notice = result.notices().single().message
    assertTrue(notice.contains("2"))
    assertTrue(notice.contains("5"))
  }

  @Test fun emptyExtractionExplainsMissingText() {
    val result = OcrExtractionResult(OcrSourceKind.IMAGE, listOf(OcrPageText(1, "")))
    assertEquals(1, result.notices().size)
  }
}
