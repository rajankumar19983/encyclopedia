package com.rajankumar.encyclopaedia.feature.importer

fun OcrDocumentImport.allNotices(): List<String> = buildList {
  addAll(extraction.notices().map { it.message })
  addAll(review.session.notices().map { it.message })
}

fun OcrExtractionResult.metadataAudit(): OcrMetadataAudit = auditOcrMetadata(orderedPages)

data class OcrExtractionNotice(val message: String)

fun OcrExtractionResult.notices(): List<OcrExtractionNotice> = buildList {
  if (failedPages.isNotEmpty()) {
    add(OcrExtractionNotice("OCR failed on page${if (failedPages.size == 1) "" else "s"} ${failedPages.joinToString { it.pageNumber.toString() }}. Review the import before saving."))
  }
  if (combinedText.isBlank()) add(OcrExtractionNotice("No usable English OCR text was extracted."))
}

enum class OcrSourceKind { IMAGE, PDF }

data class OcrPageText(
  val pageNumber: Int,
  val text: String,
  val error: String? = null
)

data class OcrExtractionResult(
  val sourceKind: OcrSourceKind,
  val pages: List<OcrPageText>
) {
  val orderedPages: List<OcrPageText> get() = pages.sortedBy { it.pageNumber }
  val successfulPages: List<OcrPageText> get() = orderedPages.filter { it.error == null }
  val failedPages: List<OcrPageText> get() = orderedPages.filter { it.error != null }
  val combinedText: String get() = successfulPages.joinToString("\n") { it.text.trim() }.trim()
  val requiresReview: Boolean get() = failedPages.isNotEmpty() || combinedText.isBlank()
}

data class OcrSourceRetentionPolicy(
  val retainOriginalMedia: Boolean = false,
  val retainExtractedTextDuringReview: Boolean = true,
  val persistExtractedTextAfterImport: Boolean = false,
)

val defaultOcrSourceRetentionPolicy = OcrSourceRetentionPolicy()
