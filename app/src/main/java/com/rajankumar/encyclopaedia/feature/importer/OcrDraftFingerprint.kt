package com.rajankumar.encyclopaedia.feature.importer

private fun normalizedFingerprintPart(value: String): String = value
  .lowercase()
  .replace(Regex("[^\\p{L}\\p{N}]+"), "")

fun ParsedQuestionDraft.fingerprint(): String = buildString {
  append(normalizedFingerprintPart(questionText))
  options.forEach { append('|').append(normalizedFingerprintPart(it)) }
}
