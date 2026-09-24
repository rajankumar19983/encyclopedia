package com.rajankumar.encyclopaedia.feature.importer

data class OcrImportSanitization(
  val text: String,
  val removedDevanagari: Boolean
)

fun sanitizeOcrImportText(rawText: String): OcrImportSanitization = OcrImportSanitization(
  text = OcrEnglishTextFilter.filter(rawText),
  removedDevanagari = OcrEnglishTextFilter.containsDevanagari(rawText)
)
