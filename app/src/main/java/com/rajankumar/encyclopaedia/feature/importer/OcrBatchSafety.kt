package com.rajankumar.encyclopaedia.feature.importer

data class OcrBatchSafety(val safeToReview: Boolean, val message: String)

fun OcrBatchDiagnostics.safety(): OcrBatchSafety = if (needsReview) {
  OcrBatchSafety(false, message() ?: "OCR batch structure needs review before trusting the extracted drafts.")
} else {
  OcrBatchSafety(true, "OCR batch structure is consistent. Review each draft before saving.")
}
