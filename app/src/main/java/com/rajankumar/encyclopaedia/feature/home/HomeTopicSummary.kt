package com.rajankumar.encyclopaedia.feature.home

data class HomeTopicSummary(val total: Int, val completed: Int) {
  val progress: HomeProgressSummary get() = HomeProgressSummary(completed, total)
  val label: String get() = "${total.coerceAtLeast(0)} topics"
}
