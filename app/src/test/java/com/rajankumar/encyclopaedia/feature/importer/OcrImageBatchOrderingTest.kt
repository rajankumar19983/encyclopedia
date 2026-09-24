package com.rajankumar.encyclopaedia.feature.importer

import org.junit.Assert.assertEquals
import org.junit.Test

class OcrImageBatchOrderingTest {
  @Test fun combinedImportsReceiveContinuousPageNumbers() {
    val firstExtraction = OcrExtractionResult(
      OcrSourceKind.IMAGE,
      listOf(OcrPageText(1, "1. First question?\nA. A\nB. B"), OcrPageText(2, "2. Second question?\nA. A\nB. B"))
    )
    val secondExtraction = OcrExtractionResult(
      OcrSourceKind.IMAGE,
      listOf(OcrPageText(1, "3. Third question?\nA. A\nB. B"))
    )
    val combined = combineImageImports(
      listOf(
        OcrDocumentImport(firstExtraction, prepareOcrImportReview(firstExtraction.combinedText)),
        OcrDocumentImport(secondExtraction, prepareOcrImportReview(secondExtraction.combinedText))
      )
    )
    assertEquals(listOf(1, 2, 3), combined.extraction.orderedPages.map { it.pageNumber })
  }
}
