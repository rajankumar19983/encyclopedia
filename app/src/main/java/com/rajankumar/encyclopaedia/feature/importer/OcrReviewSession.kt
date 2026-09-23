package com.rajankumar.encyclopaedia.feature.importer

data class OcrReviewSession(
  val draftCount: Int,
  val approved: Int = 0,
  val rejected: Int = 0,
) {
  val pending: Int get() = (draftCount - approved - rejected).coerceAtLeast(0)
  val complete: Boolean get() = draftCount > 0 && pending == 0
}
