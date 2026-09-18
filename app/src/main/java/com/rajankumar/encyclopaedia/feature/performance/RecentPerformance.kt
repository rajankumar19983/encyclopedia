package com.rajankumar.encyclopaedia.feature.performance

import com.rajankumar.encyclopaedia.data.local.QuestionAttemptEntity

data class RecentPerformance(val attempts: Int, val correct: Int) {
  val accuracy get() = if (attempts == 0) 0 else correct * 100 / attempts
}

fun List<QuestionAttemptEntity>.recentPerformance(limit: Int = 20): RecentPerformance {
  val recent = sortedByDescending { it.attemptedAt }.take(limit.coerceAtLeast(1))
  return RecentPerformance(recent.size, recent.count { it.isCorrect })
}
