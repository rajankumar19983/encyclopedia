package com.rajankumar.encyclopaedia.feature.home

data class HomePlanProgress(val completed: Int, val total: Int) {
  val safeCompleted = completed.coerceIn(0, total.coerceAtLeast(0))
  val fraction: Float get() = if (total <= 0) 0f else safeCompleted.toFloat() / total
  val label: String get() = "$safeCompleted of ${total.coerceAtLeast(0)} completed"
}
