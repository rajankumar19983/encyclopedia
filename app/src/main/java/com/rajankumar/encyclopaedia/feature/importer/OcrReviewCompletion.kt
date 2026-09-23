package com.rajankumar.encyclopaedia.feature.importer

data class OcrReviewCompletion(val complete: Boolean, val unresolvedIndexes: List<Int>)

fun reviewCompletion(decisions: List<OcrReviewDecision>): OcrReviewCompletion {
  val unresolved = decisions.mapIndexedNotNull { index, decision -> index.takeIf { decision == OcrReviewDecision.PENDING } }
  return OcrReviewCompletion(decisions.isNotEmpty() && unresolved.isEmpty(), unresolved)
}
