package com.rajankumar.encyclopaedia.feature.importer

import android.content.ContentResolver
import android.net.Uri

class OcrDocumentImporter(contentResolver: ContentResolver) {
  private val imageExtractor = ImageOcrExtractor(contentResolver)
  private val pdfExtractor = PdfOcrExtractor(contentResolver)

  suspend fun importImage(uri: Uri): OcrDocumentImport = buildImport(imageExtractor.extract(uri))

  suspend fun importPdf(uri: Uri): OcrDocumentImport = buildImport(pdfExtractor.extract(uri))

  private fun buildImport(extraction: OcrExtractionResult): OcrDocumentImport {
    val review = prepareOcrImportReview(extraction.combinedText)
    return OcrDocumentImport(extraction, review)
  }
}

data class OcrDocumentImport(
  val extraction: OcrExtractionResult,
  val review: OcrImportReviewBundle
) {
  val requiresReview: Boolean get() = extraction.requiresReview || review.session.summary.needsAttention > 0
}
