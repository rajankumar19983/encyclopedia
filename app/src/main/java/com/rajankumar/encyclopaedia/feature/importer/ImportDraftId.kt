package com.rajankumar.encyclopaedia.feature.importer

fun importDraftId(question: String, index: Int): String {
  val stem = question.trim().lowercase()
    .replace(Regex("[^a-z0-9]+"), "-")
    .trim('-')
    .take(32)
    .ifBlank { "question" }
  return "$stem-${index + 1}"
}
