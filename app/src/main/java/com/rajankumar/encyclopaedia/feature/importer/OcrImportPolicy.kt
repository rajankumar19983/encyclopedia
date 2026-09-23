package com.rajankumar.encyclopaedia.feature.importer

data class OcrImportPolicyResult(
  val canConfirm: Boolean,
  val reasons: List<String>,
)

fun ParsedQuestionDraft.importPolicy(): OcrImportPolicyResult {
  val reviewed = withQualityWarnings()
  val optionPolicy = reviewed.optionCountPolicy()
  val language = reviewed.languageAudit()
  val reasons = buildList {
    if (reviewed.questionText.isBlank()) add("Question text is missing.")
    optionPolicy.warning?.let(::add)
    if (!language.clean) add("Hindi/Devanagari OCR content must be removed before approval.")
    if (reviewed.correctAnswer.isNullOrBlank()) add("Correct answer must be confirmed.")
    addAll(reviewed.warnings)
  }.distinct()
  return OcrImportPolicyResult(canConfirm = reasons.isEmpty(), reasons = reasons)
}
