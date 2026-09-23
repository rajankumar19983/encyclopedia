package com.rajankumar.encyclopaedia.feature.importer

data class OcrReviewChecklistProgress(val required: Int, val completed: Int) {
  val complete: Boolean get() = required > 0 && completed >= required
}

fun OcrReviewChecklistState.progress(items: List<OcrReviewChecklistItem>): OcrReviewChecklistProgress {
  val requiredIndexes = items.indices.filter { items[it].required }
  return OcrReviewChecklistProgress(requiredIndexes.size, requiredIndexes.count { it in checked })
}
