package com.rajankumar.encyclopaedia.feature.importer

fun OcrReviewDecision.accessibilityLabel(position: Int): String = when (this) {
  OcrReviewDecision.PENDING -> "Draft $position pending review"
  OcrReviewDecision.APPROVED -> "Draft $position approved and saved"
  OcrReviewDecision.REJECTED -> "Draft $position rejected and not saved"
}
