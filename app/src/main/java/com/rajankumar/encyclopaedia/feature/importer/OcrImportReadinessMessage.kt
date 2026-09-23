package com.rajankumar.encyclopaedia.feature.importer

fun OcrImportReadinessSummary.message(): String = when {
  total == 0 -> "No review drafts were produced."
  needsAttention == 0 -> "$total ${if (total == 1) "draft is" else "drafts are"} structurally ready for manual review."
  else -> "$ready of $total drafts are structurally ready • $needsAttention need attention before approval."
}
