package com.rajankumar.encyclopaedia.feature.importer

enum class OcrReviewFilter { ALL, PENDING, APPROVED, REJECTED }

fun OcrReviewFilter.matches(decision: OcrReviewDecision): Boolean = when (this) {
  OcrReviewFilter.ALL -> true
  OcrReviewFilter.PENDING -> decision == OcrReviewDecision.PENDING
  OcrReviewFilter.APPROVED -> decision == OcrReviewDecision.APPROVED
  OcrReviewFilter.REJECTED -> decision == OcrReviewDecision.REJECTED
}
