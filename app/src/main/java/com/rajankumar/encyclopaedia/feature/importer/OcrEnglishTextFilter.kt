package com.rajankumar.encyclopaedia.feature.importer

/** Removes Devanagari OCR content before MCQ parsing while preserving printed English structure. */
object OcrEnglishTextFilter {
  private val devanagari = Regex("[\\u0900-\\u097F]")
  private val whitespace = Regex("[ \\t]+")

  fun filter(rawText: String): String = rawText.lineSequence()
    .map(::filterLine)
    .filter { it.isNotBlank() }
    .joinToString("\n")

  fun containsDevanagari(value: String): Boolean = devanagari.containsMatchIn(value)

  private fun filterLine(line: String): String = line
    .replace(devanagari, " ")
    .replace(whitespace, " ")
    .trim()
}
