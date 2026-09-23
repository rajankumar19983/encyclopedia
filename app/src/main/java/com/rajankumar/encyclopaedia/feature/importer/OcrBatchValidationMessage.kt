package com.rajankumar.encyclopaedia.feature.importer

fun OcrBatchValidation.message(): String = buildString {
  append("$clean of $total drafts pass automated checks")
  if (flagged > 0) append(" • $flagged flagged for review")
  if (duplicateGroups > 0) append(" • $duplicateGroups possible duplicate ${if (duplicateGroups == 1) "group" else "groups"}")
  append('.')
}
