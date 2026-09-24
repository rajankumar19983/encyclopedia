package com.rajankumar.encyclopaedia.feature.importer

data class OcrImportMilestone(
  val imageOcr: Boolean,
  val pdfOcr: Boolean,
  val englishFiltering: Boolean,
  val variableOptions: Boolean,
  val explicitReview: Boolean,
  val duplicateProtection: Boolean,
  val sourceMetadata: Boolean,
  val noMediaRetention: Boolean,
) {
  val complete: Boolean get() = imageOcr && pdfOcr && englishFiltering && variableOptions &&
    explicitReview && duplicateProtection && sourceMetadata && noMediaRetention
}

data class OcrImportNotice(val message: String)

data class OcrImportOutcome(val approved: Int, val rejected: Int, val duplicates: Int) {
  val saved: Int get() = (approved - duplicates).coerceAtLeast(0)
}

data class OcrImportPolicyResult(
  val canConfirm: Boolean,
  val reasons: List<String>,
)

data class OcrImportPreparation(
  val sanitized: OcrSanitizationResult,
  val drafts: List<ParsedQuestionDraft>,
  val metadata: OcrSourceMetadata,
  val diagnostics: OcrBatchDiagnostics,
)

data class OcrImportReadinessSummary(
  val total: Int,
  val ready: Int,
  val needsAttention: Int,
)

data class OcrImportReport(
  val draftCount: Int,
  val removedLineCount: Int,
  val metadataConfidence: OcrMetadataConfidence,
  val structuralWarning: String?,
)

data class OcrImportReviewBundle(
  val session: OcrImportSession,
  val queue: ImportReviewQueue,
)

data class OcrImportSanitization(
  val text: String,
  val removedDevanagari: Boolean,
)

data class OcrImportSession(
  val drafts: List<ParsedQuestionDraft>,
  val examName: String?,
  val removedDevanagari: Boolean,
) {
  val summary: OcrReviewSummary get() = drafts.reviewSummary()
  val status: String get() = importReviewStatus(summary)
}

fun ParsedQuestionDraft.importPolicy(): OcrImportPolicyResult {
  val reviewed = withQualityWarnings()
  val optionPolicy = reviewed.optionCountPolicy()
  val language = reviewed.languageAudit()
  val reasons = buildList {
    if (reviewed.questionText.isBlank()) add("Question text is missing.")
    optionPolicy.warning?.let(::add)
    if (!language.clean) add("Hindi/Devanagari OCR content must be removed before approval.")
    if (reviewed.correctAnswer.isNullOrBlank()) add("Correct answer must be confirmed.")
    addAll(reviewed.warnings)
  }.distinct()
  return OcrImportPolicyResult(canConfirm = reasons.isEmpty(), reasons = reasons)
}

fun prepareOcrImport(rawText: String): OcrImportPreparation {
  val sanitized = sanitizeOcrText(rawText)
  val drafts = OcrQuestionParser.parse(sanitized.text).map { it.withQualityWarnings() }
  return OcrImportPreparation(
    sanitized = sanitized,
    drafts = drafts,
    metadata = OcrSourceMetadataExtractor.extract(rawText),
    diagnostics = buildOcrBatchDiagnostics(sanitized.text, drafts.size),
  )
}

fun summarizeImportReadiness(drafts: List<ParsedQuestionDraft>): OcrImportReadinessSummary {
  val ready = drafts.count { it.importReadiness() == ImportReadiness.READY_FOR_REVIEW }
  return OcrImportReadinessSummary(drafts.size, ready, drafts.size - ready)
}

fun OcrImportReadinessSummary.message(): String = when {
  total == 0 -> "No review drafts were produced."
  needsAttention == 0 -> "$total ${if (total == 1) "draft is" else "drafts are"} structurally ready for manual review."
  else -> "$ready of $total drafts are structurally ready • $needsAttention need attention before approval."
}

fun OcrImportPreparation.report(): OcrImportReport = OcrImportReport(
  draftCount = drafts.size,
  removedLineCount = sanitized.removedLines.size,
  metadataConfidence = metadata.confidence,
  structuralWarning = diagnostics.message(),
)

fun OcrImportReport.message(): String = buildList {
  add("Prepared $draftCount review ${if (draftCount == 1) "draft" else "drafts"}.")
  if (removedLineCount > 0) {
    add("Excluded $removedLineCount suspect OCR ${if (removedLineCount == 1) "line" else "lines"}.")
  }
  structuralWarning?.let(::add)
}.joinToString(" ")

fun sanitizeOcrImportText(rawText: String): OcrImportSanitization = OcrImportSanitization(
  text = OcrEnglishTextFilter.filter(rawText),
  removedDevanagari = OcrEnglishTextFilter.containsDevanagari(rawText),
)

fun prepareOcrImportSession(rawText: String): OcrImportSession {
  val parsed = parseEnglishOcrQuestions(rawText)
  return OcrImportSession(
    drafts = parsed.drafts.map { it.withQualityWarnings() },
    examName = detectOcrExamMetadata(rawText).examName,
    removedDevanagari = parsed.removedDevanagari,
  )
}

fun OcrImportSession.notices(): List<OcrImportNotice> = buildList {
  if (removedDevanagari) add(OcrImportNotice("Hindi/Devanagari OCR text was removed before question parsing."))
  examName?.let { add(OcrImportNotice("Detected exam: $it")) }
  if (summary.needsAttention > 0) {
    add(OcrImportNotice(
      "${summary.needsAttention} draft${if (summary.needsAttention == 1) "" else "s"} require editing before approval.",
    ))
  }
}

fun OcrImportSession.toEditableReviewItems(): List<ImportReviewItem> = drafts.mapIndexed { index, draft ->
  ImportReviewItem(
    id = importDraftId(draft.questionText, index),
    draft = EditableImportDraft(
      question = draft.questionText,
      options = draft.options,
      answer = draft.correctAnswer.orEmpty(),
    ),
  )
}

fun prepareOcrImportReview(rawText: String): OcrImportReviewBundle {
  val session = prepareOcrImportSession(rawText)
  return OcrImportReviewBundle(session, ImportReviewQueue(session.toEditableReviewItems()))
}

fun OcrImportOutcome.message(): String =
  "$saved saved • $rejected rejected • $duplicates skipped as duplicate${if (duplicates == 1) "" else "s"}."
