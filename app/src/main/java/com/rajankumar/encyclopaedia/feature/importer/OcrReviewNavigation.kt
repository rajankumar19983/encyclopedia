package com.rajankumar.encyclopaedia.feature.importer

fun nextPendingReviewIndex(decisions: List<OcrReviewDecision>, afterIndex: Int): Int? {
  if (decisions.isEmpty()) return null
  val start = (afterIndex + 1).coerceAtLeast(0)
  for (index in start until decisions.size) if (decisions[index] == OcrReviewDecision.PENDING) return index
  for (index in 0 until start.coerceAtMost(decisions.size)) if (decisions[index] == OcrReviewDecision.PENDING) return index
  return null
}
