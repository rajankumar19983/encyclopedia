package com.rajankumar.encyclopaedia.feature.importer

data class OcrReviewGate(val canReview: Boolean, val reasons: List<String>)

fun OcrImportPreparation.reviewGate(): OcrReviewGate {
  val reasons = buildList {
    if (drafts.isEmpty()) add("No reviewable questions were extracted.")
    diagnostics.message()?.let(::add)
  }
  return OcrReviewGate(canReview = drafts.isNotEmpty(), reasons = reasons)
}
