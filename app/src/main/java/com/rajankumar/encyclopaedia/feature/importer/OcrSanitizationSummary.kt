package com.rajankumar.encyclopaedia.feature.importer

data class OcrSanitizationSummary(val removedCount: Int, val devanagariCount: Int, val noiseCount: Int)

fun OcrSanitizationResult.summary(): OcrSanitizationSummary = OcrSanitizationSummary(
  removedCount = removedLines.size,
  devanagariCount = removedLines.count { it.reason == "contains Devanagari" },
  noiseCount = removedLines.count { it.reason == "mostly OCR noise" },
)

fun OcrSanitizationSummary.message(): String? = when {
  removedCount -> null
  else -> "Excluded $removedCount suspect OCR lines from parsing • $devanagariCount Devanagari • $noiseCount noise."
}
