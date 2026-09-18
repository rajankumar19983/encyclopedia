package com.rajankumar.encyclopaedia.feature.performance

data class PerformanceSummary(
  val attempts: Int,
  val correct: Int,
  val uniqueQuestions: Int,
  val totalQuestions: Int,
  val averageTimeMs: Long
) {
  val accuracy: Int get() = if (attempts == 0) 0 else (correct * 100 / attempts).coerceIn(0, 100)
  val coverage: Int get() = if (totalQuestions == 0) 0 else (uniqueQuestions * 100 / totalQuestions).coerceIn(0, 100)
}
