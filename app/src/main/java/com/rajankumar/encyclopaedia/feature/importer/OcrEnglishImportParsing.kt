package com.rajankumar.encyclopaedia.feature.importer

data class OcrImportParseResult(
  val drafts: List<ParsedQuestionDraft>,
  val removedDevanagari: Boolean
)

fun parseEnglishOcrQuestions(rawText: String): OcrImportParseResult {
  val sanitized = sanitizeOcrImportText(rawText)
  return OcrImportParseResult(
    drafts = OcrQuestionParser.parse(sanitized.text),
    removedDevanagari = sanitized.removedDevanagari
  )
}
