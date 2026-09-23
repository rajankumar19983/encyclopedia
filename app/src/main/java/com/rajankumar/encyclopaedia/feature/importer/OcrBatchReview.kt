package com.rajankumar.encyclopaedia.feature.importer

data class OcrBatchReviewProgress(
  val total: Int,
  val approved: Int,
  val rejected: Int,
) {
  val resolved: Int get() = approved + rejected
  val pending: Int get() = (total - resolved).coerceAtLeast(0)
  val complete: Boolean get() = total > 0 && pending == 0
}

fun batchReviewProgress(decisions: List<OcrReviewDecision>): OcrBatchReviewProgress = OcrBatchReviewProgress(
  total = decisions.size,
  approved = decisions.count { it == OcrReviewDecision.APPROVED },
  rejected = decisions.count { it == OcrReviewDecision.REJECTED },
)
