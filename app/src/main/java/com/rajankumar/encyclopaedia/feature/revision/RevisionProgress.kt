package com.rajankumar.encyclopaedia.feature.revision

data class RevisionProgress(val completed: Int, val total: Int) {
  val safeCompleted: Int get() = completed.coerceIn(0, total.coerceAtLeast(0))
  val remaining: Int get() = (total.coerceAtLeast(0) - safeCompleted).coerceAtLeast(0)
  val percent: Int get() = if (total <= 0) 0 else safeCompleted * 100 / total
  val isComplete: Boolean get() = total > 0 && safeCompleted >= total
}
