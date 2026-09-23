package com.rajankumar.encyclopaedia.feature.performance

data class PerformanceCoverage(val practised: Int, val total: Int) {
  val remaining: Int get() = (total - practised).coerceAtLeast(0)
  val percent: Int get() = if (total <= 0) 0 else ((practised.coerceIn(0, total) * 100f) / total).toInt()
  val complete: Boolean get() = total > 0 && remaining == 0
}
