package com.rajankumar.encyclopaedia.feature.questions

data class PracticeProgress(val current: Int, val total: Int) {
  val fraction: Float get() = if (total <= 0) 0f else (current.toFloat() / total).coerceIn(0f, 1f)
  val label: String get() = "$current / $total"
}
