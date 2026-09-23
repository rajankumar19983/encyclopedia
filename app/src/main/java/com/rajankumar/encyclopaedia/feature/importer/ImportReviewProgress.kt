package com.rajankumar.encyclopaedia.feature.importer

data class ImportReviewProgress(
  val total: Int,
  val saved: Int,
  val rejected: Int,
  val remaining: Int,
)

fun importReviewProgress(total: Int, saved: Int, rejected: Int): ImportReviewProgress {
  val safeTotal = total.coerceAtLeast(0)
  val safeSaved = saved.coerceIn(0, safeTotal)
  val safeRejected = rejected.coerceIn(0, safeTotal - safeSaved)
  return ImportReviewProgress(safeTotal, safeSaved, safeRejected, safeTotal - safeSaved - safeRejected)
}
