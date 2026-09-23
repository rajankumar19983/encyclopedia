package com.rajankumar.encyclopaedia.feature.importer

data class OcrExitGuard(val requiresConfirmation: Boolean, val message: String?)

fun OcrReviewSession.exitGuard(): OcrExitGuard = if (pending > 0) {
  OcrExitGuard(true, "$pending OCR ${if (pending == 1) "draft has" else "drafts have"} no Approve/Reject decision. Leaving will discard this review state.")
} else {
  OcrExitGuard(false, null)
}
