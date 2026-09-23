package com.rajankumar.encyclopaedia.feature.importer

data class EditableImportDraft(
  val question: String,
  val options: List<String>,
  val answer: String
) {
  val cleanedOptions: List<String> get() = options.map(String::trim).filter(String::isNotBlank)

  fun isValid(): Boolean = validateForSave().canSave
}
