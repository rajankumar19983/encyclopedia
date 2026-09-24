package com.rajankumar.encyclopaedia.feature.importer

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class OcrDocumentImportTest {
  @Test fun cleanParsedExtractionStillRequiresExplicitDraftReview() {
    val extraction = OcrExtractionResult(OcrSourceKind.IMAGE, listOf(OcrPageText(1,
      "1. Which memory is volatile?\nA. RAM\nB. ROM\nAnswer: A"
    )))
    val document = OcrDocumentImport(extraction, prepareOcrImportReview(extraction.combinedText))
    assertFalse(extraction.requiresReview)
    assertTrue(document.review.queue.items.isNotEmpty())
  }

  @Test fun extractionFailurePropagatesToDocumentReviewState() {
    val extraction = OcrExtractionResult(OcrSourceKind.PDF, listOf(OcrPageText(1, "", "failed")))
    val document = OcrDocumentImport(extraction, prepareOcrImportReview(extraction.combinedText))
    assertTrue(document.requiresReview)
  }
}
