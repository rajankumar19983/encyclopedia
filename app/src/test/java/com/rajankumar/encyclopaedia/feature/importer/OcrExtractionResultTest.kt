package com.rajankumar.encyclopaedia.feature.importer

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class OcrExtractionResultTest {
  @Test fun pagesAreCombinedInPageNumberOrder() {
    val result = OcrExtractionResult(OcrSourceKind.PDF, listOf(
      OcrPageText(3, "third"), OcrPageText(1, "first"), OcrPageText(2, "second")
    ))
    assertEquals("first\nsecond\nthird", result.combinedText)
    assertFalse(result.requiresReview)
  }

  @Test fun failedPageRequiresReviewWithoutDiscardingGoodPages() {
    val result = OcrExtractionResult(OcrSourceKind.PDF, listOf(
      OcrPageText(1, "first"), OcrPageText(2, "", "failed"), OcrPageText(3, "third")
    ))
    assertEquals("first\nthird", result.combinedText)
    assertTrue(result.requiresReview)
    assertEquals(listOf(2), result.failedPages.map { it.pageNumber })
  }
}
