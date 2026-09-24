package com.rajankumar.encyclopaedia.feature.home

data class HomeProgressSummary(val completed: Int, val total: Int) {
  val percent: Int get() = if (total <= 0) 0 else (completed.coerceIn(0, total) * 100 / total)
  val fraction: Float get() = percent / 100f
}
