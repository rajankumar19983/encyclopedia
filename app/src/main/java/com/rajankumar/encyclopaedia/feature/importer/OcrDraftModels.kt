package com.rajankumar.encyclopaedia.feature.importer

data class OcrApprovalGate(val allowed: Boolean, val reasons: List<String>)

fun evaluateOcrApproval(
  validation: EditableImportValidation,
  checklistState: OcrReviewChecklistState,
  checklist: List<OcrReviewChecklistItem> = ocrReviewChecklist(),
): OcrApprovalGate {
  val reasons = buildList {
    if (!validation.canSave) addAll(validation.issues)
    if (!checklistState.requiredComplete(checklist)) add("Complete every required review check before approval.")
  }.distinct()
  return OcrApprovalGate(reasons.isEmpty(), reasons)
}
data class OcrDraftAudit(
  val optionCountQuality: OcrOptionCountQuality,
  val answerConflict: OcrAnswerConflict,
  val warnings: List<String>,
)

fun auditOcrDraft(draft: ParsedQuestionDraft, sourceLines: List<String> = emptyList()): OcrDraftAudit {
  val conflict = detectOcrAnswerConflict(sourceLines)
  return OcrDraftAudit(
    optionCountQuality = classifyOcrOptionCount(draft.options.size),
    answerConflict = conflict,
    warnings = (draft.warnings + listOfNotNull(conflict.warning())).distinct(),
  )
}

data class OcrDraftEditState(val question: String, val optionsText: String, val answer: String) {
  fun editable(): EditableImportDraft = EditableImportDraft(question, optionsText.lines(), answer)
  fun validation(): EditableImportValidation = editable().validateForSave()
}

fun ParsedQuestionDraft.toEditState(): OcrDraftEditState = OcrDraftEditState(questionText, options.joinToString("\n"), correctAnswer.orEmpty())

private fun normalizedFingerprintPart(value: String): String = value
  .lowercase()
  .replace(Regex("[^\\p{L}\\p{N}]+"), "")

fun ParsedQuestionDraft.fingerprint(): String = buildString {
  append(normalizedFingerprintPart(questionText))
  options.forEach { append('|').append(normalizedFingerprintPart(it)) }
}

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

data class OcrDraftReviewState(
  val decision: OcrReviewDecision = OcrReviewDecision.PENDING,
  val checklist: OcrReviewChecklistState = OcrReviewChecklistState(),
  val saveStatus: OcrDraftSaveStatus = OcrDraftSaveStatus.READY,
)

enum class OcrDraftSaveStatus { READY, SAVING, SAVED, DUPLICATE, FAILED }

data class OcrDraftValidation(val warnings: List<String>) {
  val clean: Boolean get() = warnings.isEmpty()
}

fun validateOcrDraft(draft: ParsedQuestionDraft): OcrDraftValidation {
  val warnings = buildList {
    addAll(draft.warnings)
    detectDuplicateOcrOptions(draft.options).warning()?.let(::add)
    val answer = checkOcrAnswerRange(draft.correctAnswer, draft.options.size)
    if (!answer.valid) add("Correct answer does not map to one of the extracted options.")
    when (classifyOcrQuestionLength(draft.questionText)) {
      OcrQuestionLengthQuality.TOO_SHORT -> add("Question text is unusually short; verify OCR.")
      OcrQuestionLengthQuality.VERY_LONG -> add("Question text is unusually long; verify question boundaries.")
      OcrQuestionLengthQuality.NORMAL -> Unit
    }
    if (classifyOcrOptionCount(draft.options.size) == OcrOptionCountQuality.UNUSUALLY_MANY) add("Unusually many options were extracted; verify question boundaries.")
  }
  return OcrDraftValidation(warnings.distinct())
}

data class OcrDuplicateGroup(val fingerprint: String, val indexes: List<Int>)

fun duplicateOcrDraftGroups(drafts: List<ParsedQuestionDraft>): List<OcrDuplicateGroup> = drafts
  .mapIndexed { index, draft -> draft.fingerprint() to index }
  .groupBy({ it.first }, { it.second })
  .filterValues { it.size > 1 }
  .map { (fingerprint, indexes) -> OcrDuplicateGroup(fingerprint, indexes) }
  .sortedBy { it.indexes.first() }

fun duplicateOcrWarning(drafts: List<ParsedQuestionDraft>): String? {
  val groups = duplicateOcrDraftGroups(drafts)
  if (groups.isEmpty()) return null
  val positions = groups.joinToString("; ") { group -> group.indexes.joinToString(", ") { (it + 1).toString() } }
  return "Possible duplicate OCR drafts detected at review positions: $positions. Verify before saving."
}

data class OcrExitGuard(val requiresConfirmation: Boolean, val message: String?)

data class OcrReviewNavigationGuard(
  val blocked: Boolean,
  val requiresConfirmation: Boolean,
  val message: String?,
)

fun buildOcrReviewNavigationGuard(
  drafts: List<OcrDraftReviewState>,
  extractionInProgress: Boolean = false,
): OcrReviewNavigationGuard {
  if (extractionInProgress) {
    return OcrReviewNavigationGuard(
      blocked = true,
      requiresConfirmation = false,
      message = "Wait for OCR extraction to finish before leaving this screen.",
    )
  }
  val saving = drafts.count { it.saveStatus == OcrDraftSaveStatus.SAVING }
  if (saving > 0) {
    return OcrReviewNavigationGuard(
      blocked = true,
      requiresConfirmation = false,
      message = "Wait for $saving ${if (saving == 1) "question" else "questions"} to finish saving before leaving or choosing another source.",
    )
  }
  val decisions = drafts.map { it.decision }
  val pending = decisions.count { it == OcrReviewDecision.PENDING }
  return OcrReviewNavigationGuard(
    blocked = false,
    requiresConfirmation = pending > 0,
    message = if (pending > 0) {
      "$pending OCR ${if (pending == 1) "draft has" else "drafts have"} no Approve/Reject decision. Continuing will discard this review state."
    } else {
      null
    },
  )
}

fun OcrReviewSession.exitGuard(): OcrExitGuard = if (pending > 0) {
  OcrExitGuard(true, "$pending OCR ${if (pending == 1) "draft has" else "drafts have"} no Approve/Reject decision. Leaving will discard this review state.")
} else {
  OcrExitGuard(false, null)
}

fun OcrDraftSaveStatus.label(): String = when (this) {
  OcrDraftSaveStatus.READY -> "Approve & Save"
  OcrDraftSaveStatus.SAVING -> "Checking…"
  OcrDraftSaveStatus.SAVED -> "Saved"
  OcrDraftSaveStatus.DUPLICATE -> "Already exists"
  OcrDraftSaveStatus.FAILED -> "Retry Save"
}
