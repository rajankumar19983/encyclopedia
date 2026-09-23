package com.rajankumar.encyclopaedia.feature.importer

data class OcrReviewUiSummary(val headline: String, val detail: String)

fun buildOcrReviewUiSummary(decisions: List<OcrReviewDecision>): OcrReviewUiSummary {
  val counts = countOcrReviewDecisions(decisions)
  val headline = if (counts.pending == 0 && decisions.isNotEmpty()) "Review complete" else "${counts.pending} remaining"
  return OcrReviewUiSummary(headline, "${counts.approved} approved • ${counts.rejected} rejected • ${counts.pending} pending")
}
