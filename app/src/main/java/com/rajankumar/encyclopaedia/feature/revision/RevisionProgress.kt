package com.rajankumar.encyclopaedia.feature.revision

data class RevisionProgress(val completed: Int, val total: Int) {
  val percent: Int get() = if (total <= 0) 0 else (completed.coerceIn(0, total) * 100 / total)
}
