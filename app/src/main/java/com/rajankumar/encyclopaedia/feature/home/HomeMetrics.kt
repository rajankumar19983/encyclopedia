package com.rajankumar.encyclopaedia.feature.home

data class HomeMetrics(
  val accuracyPercent: Int,
  val coveragePercent: Int,
  val remainingQuestions: Int
)

fun calculateHomeMetrics(questionCount: Int, attemptCount: Int, correctCount: Int, practisedCount: Int): HomeMetrics {
  val accuracy = if (attemptCount > 0) (correctCount * 100f / attemptCount).toInt().coerceIn(0, 100) else 0
  val coverage = if (questionCount > 0) (practisedCount * 100f / questionCount).toInt().coerceIn(0, 100) else 0
  return HomeMetrics(accuracy, coverage, (questionCount - practisedCount).coerceAtLeast(0))
}
