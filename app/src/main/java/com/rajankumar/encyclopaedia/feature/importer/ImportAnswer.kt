package com.rajankumar.encyclopaedia.feature.importer

fun importedAnswerIndex(value: String, optionCount: Int): Int? {
  val clean = value.trim().uppercase()
  val index = clean.toIntOrNull()?.minus(1)
    ?: clean.singleOrNull()?.takeIf { it in 'A'..'Z' }?.let { it.code - 'A'.code }
  return index?.takeIf { it in 0 until optionCount }
}

fun normalizedImportedAnswer(value: String, optionCount: Int): String? =
  importedAnswerIndex(value, optionCount)?.let { ('A'.code + it).toChar().toString() }
