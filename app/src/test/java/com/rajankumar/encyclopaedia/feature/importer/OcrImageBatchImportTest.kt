package com.rajankumar.encyclopaedia.feature.importer

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class OcrImageBatchImportTest {
  private fun document(text: String, error: String? = null) = OcrDocumentImport(
    OcrExtractionResult(OcrSourceKind.IMAGE, listOf(OcrPageText(1, text, error))),
    prepareOcrImportReview(text)
  )

  @Test fun combinesSelectedImagesInSelectionOrder() {
    val combined = combineImageImports(listOf(document("first"), document("second"), document("third")))
    assertEquals("first\nsecond\nthird", combined.extraction.combinedText)
    assertEquals(listOf(1, 2, 3), combined.extraction.orderedPages.map { it.pageNumber })
  }

  @Test fun failedImageDoesNotDiscardSuccessfulImages() {
    val combined = combineImageImports(listOf(document("first"), document("", "failed"), document("third")))
    assertEquals("first\nthird", combined.extraction.combinedText)
    assertEquals(listOf(2), combined.extraction.failedPages.map { it.pageNumber })
    assertTrue(combined.requiresReview)
  }
}
