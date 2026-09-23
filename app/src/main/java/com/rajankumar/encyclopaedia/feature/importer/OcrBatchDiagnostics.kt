package com.rajankumar.encyclopaedia.feature.importer

data class OcrBatchDiagnostics(
  val detectedQuestionNumbers: Int,
  val parsedDrafts: Int,
  val missingQuestionNumbers: List<Int>,
  val duplicateQuestionNumbers: List<Int>,
) {
  val needsReview: Boolean get() = missingQuestionNumbers.isNotEmpty() || duplicateQuestionNumbers.isNotEmpty() || detectedQuestionNumbers != parsedDrafts
}

fun buildOcrBatchDiagnostics(rawText: String, parsedDrafts: Int): OcrBatchDiagnostics {
  val sequence = checkQuestionSequence(extractQuestionNumbers(rawText))
  return OcrBatchDiagnostics(sequence.numbers.size, parsedDrafts, sequence.missing, sequence.duplicates)
}
