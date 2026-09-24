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
    if (options.size !in 4..6) add("Imported MCQs must contain between four and six options.")
    if (!hasOnlyEnglishOcrContent()) add("Hindi/Devanagari OCR text must be removed before saving.")
    if (answer == null) add("Choose a correct option that exists in this question.")

    val qualityDraft = ParsedQuestionDraft(question.trim(), options, answer).withQualityWarnings()
    addAll(
      qualityDraft.warnings.filterNot { warning ->
        warning == "Question does not contain usable English text." && question.isBlank()
      }
    )
  }.distinct()
  return EditableImportValidation(answer, issues)
}
