package com.rajankumar.encyclopaedia.feature.importer

fun OcrReviewCompletion.message(): String = if (complete) {
  "Every OCR draft has an explicit review decision."
} else {
  val positions = unresolvedIndexes.joinToString(", ") { (it + 1).toString() }
  "Review positions still pending: $positions."
}
