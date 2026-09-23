package com.rajankumar.encyclopaedia.feature.importer

data class EditableImportValidation(
  val normalizedAnswer: String?,
  val issues: List<String>,
) {
  val canSave: Boolean get() = issues.isEmpty() && normalizedAnswer != null
}

fun EditableImportDraft.validateForSave(): EditableImportValidation {
  val options = cleanedOptions
  val answer = normalizedImportedAnswer(answer, options.size)
  val issues = buildList {
    if (question.isBlank()) add("Question text is required.")
    if (options.size < 2) add("At least two options are required.")
    if (options.size > 6) add("At most six options are supported. Review OCR structure before saving.")
    if (!hasOnlyEnglishOcrContent()) add("Hindi/Devanagari OCR text must be removed before saving.")
    if (answer == null) add("Choose a correct option that exists in this question.")
    val qualityDraft = ParsedQuestionDraft(question.trim(), options, answer).withQualityWarnings()
    addAll(qualityDraft.warnings)
  }.distinct()
  return EditableImportValidation(answer, issues)
}
