package com.rajankumar.encyclopaedia.feature.importer

fun ParsedQuestionDraft.withQualityWarnings(): ParsedQuestionDraft {
  val warnings = buildList {
    addAll(this@withQualityWarnings.warnings)
    val questionQuality = assessOcrTextQuality(questionText)
    if (questionQuality.suspiciousReplacementCharacters > 0) add("Question contains unreadable OCR characters; review the source.")
    if (questionQuality.devanagariCharacters > 0) add("Question contains Devanagari text; remove it before import.")
    if (!questionQuality.hasUsableLatinText) add("Question does not contain usable English text.")
    options.forEachIndexed { index, option ->
      val quality = assessOcrTextQuality(option)
      if (quality.needsReview) add("Option ${('A'.code + index).toChar()} contains text that needs review.")
    }
  }
  return copy(warnings = warnings.distinct())
}
