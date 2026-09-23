package com.rajankumar.encyclopaedia.feature.importer

fun OcrBatchReviewProgress.completionGuidance(): String? = when {
  total == 0 -> null
  complete -> "Every OCR draft has an explicit decision. You can leave this import safely."
  pending == 1 -> "1 OCR draft still needs an explicit Approve or Reject decision."
  else -> "$pending OCR drafts still need explicit Approve or Reject decisions."
}
