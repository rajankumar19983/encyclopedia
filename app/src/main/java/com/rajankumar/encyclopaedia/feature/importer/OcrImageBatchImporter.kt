package com.rajankumar.encyclopaedia.feature.importer

import android.net.Uri

data class OcrImageBatchProgress(val completed: Int, val total: Int)

suspend fun OcrDocumentImporter.importImages(
  uris: List<Uri>,
  onProgress: (OcrImageBatchProgress) -> Unit = {}
): OcrDocumentImport {
  require(uris.isNotEmpty()) { "Choose at least one image." }
  val documents = uris.mapIndexed { index, uri ->
    importImage(uri).also { onProgress(OcrImageBatchProgress(index + 1, uris.size)) }
  }
  return combineImageImports(documents)
}

fun combineImageImports(documents: List<OcrDocumentImport>): OcrDocumentImport {
  require(documents.isNotEmpty()) { "At least one OCR document is required." }
  val pages = documents.flatMapIndexed { documentIndex, document ->
    document.extraction.orderedPages.mapIndexed { pageIndex, page ->
      page.copy(pageNumber = documentIndex + pageIndex + 1)
    }
  }
  val extraction = OcrExtractionResult(OcrSourceKind.IMAGE, pages)
  return OcrDocumentImport(extraction, prepareOcrImportReview(extraction.combinedText))
}
