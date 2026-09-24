package com.rajankumar.encyclopaedia.feature.importer

enum class OcrReviewDecision { PENDING, APPROVED, REJECTED }

enum class OcrReviewFilter { ALL, PENDING, APPROVED, REJECTED }

enum class OcrReviewRisk { LOW, REVIEW_REQUIRED }

data class OcrReviewChecklistItem(val label: String, val required: Boolean = true)

data class OcrReviewChecklistState(val checked: Set<Int> = emptySet()) {
  fun toggle(index: Int): OcrReviewChecklistState = copy(
    checked = if (index in checked) checked - index else checked + index,
  )

  fun requiredComplete(items: List<OcrReviewChecklistItem>): Boolean = items.indices
    .filter { items[it].required }
    .all { it in checked }
}

data class OcrReviewChecklistProgress(val required: Int, val completed: Int) {
  val complete: Boolean get() = required > 0 && completed >= required
}

data class OcrReviewCompletion(val complete: Boolean, val unresolvedIndexes: List<Int>)

data class OcrReviewCounts(val pending: Int, val approved: Int, val rejected: Int)

data class OcrReviewSession(
  val draftCount: Int,
  val approved: Int = 0,
  val rejected: Int = 0,
) {
  val pending: Int get() = (draftCount - approved - rejected).coerceAtLeast(0)
  val complete: Boolean get() = draftCount > 0 && pending == 0
}

data class OcrReviewSummary(val total: Int, val ready: Int, val needsAttention: Int) {
  val canConfirmAll: Boolean get() = total > 0 && needsAttention == 0
}

data class OcrReviewUiSummary(val headline: String, val detail: String)

data class OcrReviewBatchState(val drafts: List<OcrDraftReviewState>) {
  val decisions: List<OcrReviewDecision> get() = drafts.map { it.decision }
  val summary: OcrReviewUiSummary get() = buildOcrReviewUiSummary(decisions)
  val completion: OcrReviewCompletion get() = reviewCompletion(decisions)
}

data class OcrReviewGate(val canReview: Boolean, val reasons: List<String>)

const val OCR_SOURCE_CHECKLIST_INDEX = 3

fun ocrReviewChecklist(): List<OcrReviewChecklistItem> = listOf(
  OcrReviewChecklistItem("Question text matches the printed source"),
  OcrReviewChecklistItem("All options are present and in the correct order"),
  OcrReviewChecklistItem("Correct answer was verified"),
  OcrReviewChecklistItem("Exam/source metadata was verified", required = false),
  OcrReviewChecklistItem("No unwanted Hindi/handwritten content remains"),
)

fun OcrReviewChecklistState.progress(items: List<OcrReviewChecklistItem>): OcrReviewChecklistProgress {
  val requiredIndexes = items.indices.filter { items[it].required }
  return OcrReviewChecklistProgress(requiredIndexes.size, requiredIndexes.count { it in checked })
}

fun OcrReviewDecision.isResolved(): Boolean = this != OcrReviewDecision.PENDING

fun canTransitionOcrDecision(from: OcrReviewDecision, to: OcrReviewDecision): Boolean = when (from) {
  OcrReviewDecision.PENDING -> to != OcrReviewDecision.PENDING
  OcrReviewDecision.APPROVED, OcrReviewDecision.REJECTED -> false
}

fun OcrReviewFilter.matches(decision: OcrReviewDecision): Boolean = when (this) {
  OcrReviewFilter.ALL -> true
  OcrReviewFilter.PENDING -> decision == OcrReviewDecision.PENDING
  OcrReviewFilter.APPROVED -> decision == OcrReviewDecision.APPROVED
  OcrReviewFilter.REJECTED -> decision == OcrReviewDecision.REJECTED
}

fun OcrReviewFilter.count(decisions: List<OcrReviewDecision>): Int = decisions.count(::matches)

fun reviewCompletion(decisions: List<OcrReviewDecision>): OcrReviewCompletion {
  val unresolved = decisions.mapIndexedNotNull { index, decision ->
    index.takeIf { decision == OcrReviewDecision.PENDING }
  }
  return OcrReviewCompletion(decisions.isNotEmpty() && unresolved.isEmpty(), unresolved)
}

fun OcrReviewCompletion.message(): String = if (complete) {
  "Every OCR draft has an explicit review decision."
} else {
  val positions = unresolvedIndexes.joinToString(", ") { (it + 1).toString() }
  "Review positions still pending: $positions."
}

fun countOcrReviewDecisions(decisions: List<OcrReviewDecision>): OcrReviewCounts = OcrReviewCounts(
  pending = decisions.count { it == OcrReviewDecision.PENDING },
  approved = decisions.count { it == OcrReviewDecision.APPROVED },
  rejected = decisions.count { it == OcrReviewDecision.REJECTED },
)

fun OcrImportPreparation.reviewGate(): OcrReviewGate {
  val reasons = buildList {
    if (drafts.isEmpty()) add("No reviewable questions were extracted.")
    diagnostics.message()?.let(::add)
  }
  return OcrReviewGate(canReview = drafts.isNotEmpty(), reasons = reasons)
}

fun OcrBatchReviewProgress.message(): String = when {
  total == 0 -> "No OCR drafts to review."
  complete -> "Review complete • $approved approved • $rejected rejected."
  else -> "$resolved of $total reviewed • $pending remaining."
}

fun nextPendingReviewIndex(decisions: List<OcrReviewDecision>, afterIndex: Int): Int? {
  if (decisions.isEmpty()) return null
  val start = (afterIndex + 1).coerceAtLeast(0)
  for (index in start until decisions.size) {
    if (decisions[index] == OcrReviewDecision.PENDING) return index
  }
  for (index in 0 until start.coerceAtMost(decisions.size)) {
    if (decisions[index] == OcrReviewDecision.PENDING) return index
  }
  return null
}

fun OcrDraftAudit.reviewRisk(): OcrReviewRisk = if (
  warnings.isNotEmpty() || optionCountQuality != OcrOptionCountQuality.NORMAL || answerConflict.hasConflict
) {
  OcrReviewRisk.REVIEW_REQUIRED
} else {
  OcrReviewRisk.LOW
}

fun OcrReviewDecision.accessibilityLabel(position: Int): String = when (this) {
  OcrReviewDecision.PENDING -> "Draft $position pending review"
  OcrReviewDecision.APPROVED -> "Draft $position approved and saved"
  OcrReviewDecision.REJECTED -> "Draft $position rejected and not saved"
}

fun OcrReviewSession.message(): String = if (complete) {
  "Review complete • $approved approved • $rejected rejected."
} else {
  "$pending of $draftCount drafts still need an explicit decision."
}

fun List<ParsedQuestionDraft>.reviewSummary(): OcrReviewSummary {
  val ready = count { it.importReadiness() == ImportReadiness.READY_FOR_REVIEW }
  return OcrReviewSummary(size, ready, size - ready)
}

fun buildOcrReviewUiSummary(decisions: List<OcrReviewDecision>): OcrReviewUiSummary {
  val counts = countOcrReviewDecisions(decisions)
  val headline = if (counts.pending == 0 && decisions.isNotEmpty()) {
    "Review complete"
  } else {
    "${counts.pending} remaining"
  }
  return OcrReviewUiSummary(
    headline,
    "${counts.approved} approved • ${counts.rejected} rejected • ${counts.pending} pending",
  )
}
