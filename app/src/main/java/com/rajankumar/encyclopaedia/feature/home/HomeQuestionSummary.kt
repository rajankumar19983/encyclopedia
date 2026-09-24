package com.rajankumar.encyclopaedia.feature.home

data class HomeQuestionSummary(val total: Int, val attempted: Int) {
  val remaining: Int get() = (total.coerceAtLeast(0) - attempted.coerceAtLeast(0)).coerceAtLeast(0)
  val attemptedProgress: HomeProgressSummary get() = HomeProgressSummary(attempted, total)
}
