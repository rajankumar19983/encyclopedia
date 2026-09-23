package com.rajankumar.encyclopaedia.feature.importer

data class OcrTextQuality(
  val suspiciousReplacementCharacters: Int,
  val devanagariCharacters: Int,
  val hasUsableLatinText: Boolean,
) {
  val needsReview: Boolean get() = suspiciousReplacementCharacters > 0 || devanagariCharacters > 0 || !hasUsableLatinText
}

fun assessOcrTextQuality(text: String): OcrTextQuality = OcrTextQuality(
  suspiciousReplacementCharacters = text.count { it == '\uFFFD' },
  devanagariCharacters = text.count { it in '\u0900'..'\u097F' },
  hasUsableLatinText = text.any { it in 'A'..'Z' || it in 'a'..'z' },
)
