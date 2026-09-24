package com.rajankumar.encyclopaedia.feature.home

data class HomeStreakSummary(val days: Int) {
  val safeDays: Int get() = days.coerceAtLeast(0)
  val label: String get() = if (safeDays == 1) "1 day" else "$safeDays days"
}
