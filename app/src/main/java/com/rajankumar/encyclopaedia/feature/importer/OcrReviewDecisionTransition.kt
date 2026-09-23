package com.rajankumar.encyclopaedia.feature.importer

fun canTransitionOcrDecision(from: OcrReviewDecision, to: OcrReviewDecision): Boolean = when (from) {
  OcrReviewDecision.PENDING -> to != OcrReviewDecision.PENDING
  OcrReviewDecision.APPROVED, OcrReviewDecision.REJECTED -> false
}
