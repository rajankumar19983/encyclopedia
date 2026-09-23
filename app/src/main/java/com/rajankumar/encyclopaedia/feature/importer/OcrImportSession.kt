package com.rajankumar.encyclopaedia.feature.importer

data class OcrImportSession(
  val drafts: List<ParsedQuestionDraft>,
  val examName: String?,
  val removedDevanagari: Boolean
) {
  val summary: OcrReviewSummary get() = drafts.reviewSummary()
  val status: String get() = importReviewStatus(summary)
}

fun prepareOcrImportSession(rawText: String): OcrImportSession {
  val parsed = parseEnglishOcrQuestions(rawText)
  return OcrImportSession(
    drafts = parsed.drafts.map { it.withQualityWarnings() },
    examName = detectOcrExamMetadata(rawText).examName,
    removedDevanagari = parsed.removedDevanagari
  )
}
