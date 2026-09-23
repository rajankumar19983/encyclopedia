package com.rajankumar.encyclopaedia.feature.importer

fun OcrDuplicateOptions.warning(): String? {
  if (!hasDuplicates) return null
  val labels = groups.joinToString("; ") { group -> group.joinToString(", ") { ocrOptionLabel(it) } }
  return "Duplicate option text detected at: $labels. Verify OCR before approval."
}
