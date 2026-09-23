package com.rajankumar.encyclopaedia.feature.importer

data class ImportReviewQueue(val items: List<ImportReviewItem>) {
  val remaining: Int get() = items.count { !it.reviewed }
  val complete: Boolean get() = items.isNotEmpty() && remaining == 0

  fun markReviewed(id: String): ImportReviewQueue = copy(
    items = items.map { if (it.id == id) it.copy(reviewed = true) else it }
  )
}
