package com.rajankumar.encyclopaedia.feature.importer

fun OcrBatchReviewProgress.message(): String = when {
  total == 0 -> "No OCR drafts to review."
  complete -> "Review complete • $approved approved • $rejected rejected."
  else -> "$resolved of $total reviewed • $pending remaining."
}
