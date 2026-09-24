package com.rajankumar.encyclopaedia.feature.importer

data class OcrExtractionNotice(val message: String)

fun OcrExtractionResult.notices(): List<OcrExtractionNotice> = buildList {
  if (failedPages.isNotEmpty()) {
    add(OcrExtractionNotice("OCR failed on page${if (failedPages.size == 1) "" else "s"} ${failedPages.joinToString { it.pageNumber.toString() }}. Review the import before saving."))
  }
  if (combinedText.isBlank()) add(OcrExtractionNotice("No usable English OCR text was extracted."))
}
