package com.rajankumar.encyclopaedia.feature.importer

fun OcrBatchDiagnostics.message(): String? {
  if (!needsReview) return null
  return buildList {
    if (detectedQuestionNumbers != parsedDrafts) add("Detected $detectedQuestionNumbers numbered questions but produced $parsedDrafts review drafts.")
    if (missingQuestionNumbers.isNotEmpty()) add("Missing question numbers: ${missingQuestionNumbers.joinToString()}.")
    if (duplicateQuestionNumbers.isNotEmpty()) add("Duplicate question numbers: ${duplicateQuestionNumbers.joinToString()}.")
  }.joinToString(" ")
}
