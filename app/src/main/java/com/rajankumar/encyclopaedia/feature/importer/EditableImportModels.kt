package com.rajankumar.encyclopaedia.feature.importer

data class EditableImportDraft(
  val question: String,
  val options: List<String>,
  val answer: String
) {
  val cleanedOptions: List<String> get() = options.map(String::trim).filter(String::isNotBlank)

  fun isValid(): Boolean = validateForSave().canSave
}
fun EditableImportDraft.englishOnly(): EditableImportDraft = copy(
  question = OcrEnglishTextFilter.filter(question),
  options = options.map(OcrEnglishTextFilter::filter),
  answer = answer.trim()
)

fun EditableImportDraft.hasOnlyEnglishOcrContent(): Boolean =
  !OcrEnglishTextFilter.containsDevanagari(question) &&
    options.none(OcrEnglishTextFilter::containsDevanagari)

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
