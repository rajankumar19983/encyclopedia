package com.rajankumar.encyclopaedia.feature.importer

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
