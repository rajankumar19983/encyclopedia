package com.rajankumar.encyclopaedia.feature.importer

data class OcrReviewBatchState(val drafts: List<OcrDraftReviewState>) {
  val decisions: List<OcrReviewDecision> get() = drafts.map { it.decision }
  val summary: OcrReviewUiSummary get() = buildOcrReviewUiSummary(decisions)
  val completion: OcrReviewCompletion get() = reviewCompletion(decisions)
}
