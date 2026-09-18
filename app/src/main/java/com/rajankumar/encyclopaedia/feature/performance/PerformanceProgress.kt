package com.rajankumar.encyclopaedia.feature.performance

data class PerformanceProgress(val current: Int, val target: Int) {
  val fraction: Float get() = if (target <= 0) 0f else (current.toFloat() / target).coerceIn(0f, 1f)
  val reached: Boolean get() = current >= target
}
