package com.rajankumar.encyclopaedia.feature.importer

data class OcrImportPolicyResult(
  val canConfirm: Boolean,
  val reasons: List<String>,
)

fun ParsedQuestionDraft.importPolicy(): OcrImportPolicyResult {
  val reviewed = withQualityWarnings()
  val reasons = buildList {
    if (reviewed.questionText.isBlank()) add("Question text is missing.")
    if (reviewed.options.size < 2) add("At least two options are required.")
    if (reviewed.correctAnswer.isNullOrBlank()) add("Correct answer must be confirmed.")
    addAll(reviewed.warnings)
  }.distinct()
  return OcrImportPolicyResult(canConfirm = reasons.isEmpty(), reasons = reasons)
}
