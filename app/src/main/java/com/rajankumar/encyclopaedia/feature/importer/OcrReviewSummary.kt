package com.rajankumar.encyclopaedia.feature.importer

data class OcrReviewSummary(val total: Int, val ready: Int, val needsAttention: Int) {
  val canConfirmAll: Boolean get() = total > 0 && needsAttention == 0
}

fun List<ParsedQuestionDraft>.reviewSummary(): OcrReviewSummary {
  val ready = count { it.importReadiness() == ImportReadiness.READY_FOR_REVIEW }
  return OcrReviewSummary(size, ready, size - ready)
}
