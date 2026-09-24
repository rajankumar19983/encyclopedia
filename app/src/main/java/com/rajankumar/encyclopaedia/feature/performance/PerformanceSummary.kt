package com.rajankumar.encyclopaedia.feature.performance

data class PerformanceSummary(
  val attempts: Int,
  val correct: Int,
  val uniqueQuestions: Int,
  val totalQuestions: Int,
  val averageTimeMs: Long
) {
  private val safeAttempts: Int get() = attempts.coerceAtLeast(0)
  private val safeCorrect: Int get() = correct.coerceIn(0, safeAttempts)
  private val safeTotalQuestions: Int get() = totalQuestions.coerceAtLeast(0)
  private val safeUniqueQuestions: Int get() = uniqueQuestions.coerceIn(0, safeTotalQuestions)
  val accuracy: Int get() = if (safeAttempts == 0) 0 else (safeCorrect * 100 / safeAttempts).coerceIn(0, 100)
  val coverage: Int get() = if (safeTotalQuestions == 0) 0 else (safeUniqueQuestions * 100 / safeTotalQuestions).coerceIn(0, 100)
}
