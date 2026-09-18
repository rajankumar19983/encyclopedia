package com.rajankumar.encyclopaedia.feature.performance

fun trendLabel(change: Int): String = when {
  change > 0 -> "Improving"
  change < 0 -> "Declining"
  else -> "Stable"
}
