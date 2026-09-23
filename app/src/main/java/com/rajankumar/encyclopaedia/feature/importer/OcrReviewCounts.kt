package com.rajankumar.encyclopaedia.feature.importer

data class OcrReviewCounts(val pending: Int, val approved: Int, val rejected: Int)

fun countOcrReviewDecisions(decisions: List<OcrReviewDecision>): OcrReviewCounts = OcrReviewCounts(
  pending = decisions.count { it == OcrReviewDecision.PENDING },
  approved = decisions.count { it == OcrReviewDecision.APPROVED },
  rejected = decisions.count { it == OcrReviewDecision.REJECTED },
)
