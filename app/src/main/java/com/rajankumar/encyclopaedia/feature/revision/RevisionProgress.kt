package com.rajankumar.encyclopaedia.feature.revision

data class RevisionProgress(val completed: Int, val total: Int) {
  val safeTotal: Int get() = total.coerceAtLeast(0)
  val safeCompleted: Int get() = completed.coerceIn(0, safeTotal)
  val remaining: Int get() = safeTotal - safeCompleted
  val percent: Int get() = if (safeTotal == 0) 0 else safeCompleted * 100 / safeTotal
  val isComplete: Boolean get() = safeTotal > 0 && safeCompleted == safeTotal
  val summary: String get() = if (safeTotal == 0) "No revision questions in this session" else "$safeCompleted of $safeTotal completed"
}
