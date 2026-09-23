package com.rajankumar.encyclopaedia.feature.importer

data class OcrRemovedLinePreview(val text: String, val reason: String)

fun OcrSanitizationResult.removedLinePreviews(limit: Int = 8): List<OcrRemovedLinePreview> = removedLines
  .take(limit.coerceAtLeast(0))
  .map { OcrRemovedLinePreview(it.text, it.reason ?: "excluded by OCR quality checks") }
