package com.rajankumar.encyclopaedia.feature.importer

fun importReviewStatus(summary: OcrReviewSummary): String = when {
  summary.total == 0 -> "No reliably structured English MCQs were found. Nothing has been saved."
  summary.needsAttention > 0 -> "${summary.total} drafts found • ${summary.needsAttention} need attention before approval."
  else -> "${summary.total} drafts found and ready for review. Nothing is saved until you approve it."
}
