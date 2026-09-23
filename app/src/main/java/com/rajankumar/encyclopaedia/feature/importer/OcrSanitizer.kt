package com.rajankumar.encyclopaedia.feature.importer

data class OcrSanitizationResult(val text: String, val removedLines: List<OcrLineQuality>)

fun sanitizeOcrText(raw: String): OcrSanitizationResult {
  val assessed = raw.lines().map(::assessOcrLine)
  return OcrSanitizationResult(
    text = assessed.filter { it.usable }.joinToString("\n") { it.text }.trim(),
    removedLines = assessed.filter { !it.usable && it.text.isNotBlank() },
  )
}
