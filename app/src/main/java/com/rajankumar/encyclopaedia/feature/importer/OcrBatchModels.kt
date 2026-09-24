package com.rajankumar.encyclopaedia.feature.importer

data class OcrBatchDiagnostics(
  val detectedQuestionNumbers: Int,
  val parsedDrafts: Int,
  val missingQuestionNumbers: List<Int>,
  val duplicateQuestionNumbers: List<Int>,
) {
  val needsReview: Boolean get() = missingQuestionNumbers.isNotEmpty() ||
    duplicateQuestionNumbers.isNotEmpty() || detectedQuestionNumbers != parsedDrafts
}

data class OcrBatchReviewProgress(
  val total: Int,
  val approved: Int,
  val rejected: Int,
) {
  val resolved: Int get() = approved + rejected
  val pending: Int get() = (total - resolved).coerceAtLeast(0)
  val complete: Boolean get() = total > 0 && pending == 0
}

data class OcrBatchSafety(val safeToReview: Boolean, val message: String)

data class OcrBatchValidation(
  val total: Int,
  val clean: Int,
  val flagged: Int,
  val duplicateGroups: Int,
)

fun buildOcrBatchDiagnostics(rawText: String, parsedDrafts: Int): OcrBatchDiagnostics {
  val sequence = checkQuestionSequence(extractQuestionNumbers(rawText))
  return OcrBatchDiagnostics(sequence.numbers.size, parsedDrafts, sequence.missing, sequence.duplicates)
}

fun batchReviewProgress(decisions: List<OcrReviewDecision>): OcrBatchReviewProgress = OcrBatchReviewProgress(
  total = decisions.size,
  approved = decisions.count { it == OcrReviewDecision.APPROVED },
  rejected = decisions.count { it == OcrReviewDecision.REJECTED },
)

fun OcrBatchReviewProgress.completionGuidance(): String? = when {
  total == 0 -> null
  complete -> "Every OCR draft has an explicit decision. You can leave this import safely."
  pending == 1 -> "1 OCR draft still needs an explicit Approve or Reject decision."
  else -> "$pending OCR drafts still need explicit Approve or Reject decisions."
}

fun OcrBatchDiagnostics.safety(): OcrBatchSafety = if (needsReview) {
  OcrBatchSafety(false, message() ?: "OCR batch structure needs review before trusting the extracted drafts.")
} else {
  OcrBatchSafety(true, "OCR batch structure is consistent. Review each draft before saving.")
}

fun validateOcrBatch(drafts: List<ParsedQuestionDraft>): OcrBatchValidation {
  val clean = drafts.count { validateOcrDraft(it).clean }
  return OcrBatchValidation(
    total = drafts.size,
    clean = clean,
    flagged = drafts.size - clean,
    duplicateGroups = duplicateOcrDraftGroups(drafts).size,
  )
}

fun OcrBatchValidation.message(): String = buildString {
  append("$clean of $total drafts pass automated checks")
  if (flagged > 0) append(" • $flagged flagged for review")
  if (duplicateGroups > 0) {
    append(" • $duplicateGroups possible duplicate ${if (duplicateGroups == 1) "group" else "groups"}")
  }
  append('.')
}
