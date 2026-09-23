package com.rajankumar.encyclopaedia.feature.importer

object OcrLanguageFilter {
  private val devanagari = Regex("[\\u0900-\\u097F]")

  fun removeDevanagariLines(text: String): String = text
    .lines()
    .filterNot { devanagari.containsMatchIn(it) }
    .joinToString("\n")
    .trim()

  fun containsDevanagari(text: String): Boolean = devanagari.containsMatchIn(text)
}
