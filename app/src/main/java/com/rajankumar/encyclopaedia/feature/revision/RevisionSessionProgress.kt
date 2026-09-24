package com.rajankumar.encyclopaedia.feature.revision

data class RevisionSessionProgress(val completed: Int, val total: Int) {
  val safeTotal: Int get() = total.coerceAtLeast(0)
  val safeCompleted: Int get() = completed.coerceIn(0, safeTotal)
  val percent: Int get() = if (safeTotal == 0) 0 else safeCompleted * 100 / safeTotal
}
