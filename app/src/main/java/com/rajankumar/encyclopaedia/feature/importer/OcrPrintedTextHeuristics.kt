package com.rajankumar.encyclopaedia.feature.importer

data class OcrLineQuality(val text: String, val usable: Boolean, val reason: String? = null)

fun assessOcrLine(line: String): OcrLineQuality {
  val text = line.trim()
  if (text.isBlank()) return OcrLineQuality(text, false, "blank")
  if (OcrLanguageFilter.containsDevanagari(text)) return OcrLineQuality(text, false, "contains Devanagari")
  val visible = text.count { !it.isWhitespace() }
  val lettersOrDigits = text.count { it.isLetterOrDigit() }
  if (visible >= 4 && lettersOrDigits * 100 / visible < 35) return OcrLineQuality(text, false, "mostly OCR noise")
  return OcrLineQuality(text, true)
}
