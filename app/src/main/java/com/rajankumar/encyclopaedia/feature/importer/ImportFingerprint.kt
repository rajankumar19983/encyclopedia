package com.rajankumar.encyclopaedia.feature.importer

private val duplicateWhitespace = Regex("\\s+")
private val duplicatePunctuation = Regex("[^a-z0-9 ]")

fun normalizeImportFingerprintText(value: String): String = value
  .lowercase()
  .replace(duplicatePunctuation, " ")
  .replace(duplicateWhitespace, " ")
  .trim()

fun EditableImportDraft.importFingerprint(): String = buildString {
  append(normalizeImportFingerprintText(question))
  cleanedOptions.forEach { option ->
    append('|')
    append(normalizeImportFingerprintText(option))
  }
}
