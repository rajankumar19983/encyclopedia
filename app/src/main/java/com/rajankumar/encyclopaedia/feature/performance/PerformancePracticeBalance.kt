package com.rajankumar.encyclopaedia.feature.performance

data class PracticeBalance(val repeatedAttempts: Int, val firstAttempts: Int)

fun PerformanceSummary.practiceBalance(): PracticeBalance {
  val safeAttempts = attempts.coerceAtLeast(0)
  val first = uniqueQuestions.coerceIn(0, safeAttempts)
  return PracticeBalance((safeAttempts - first).coerceAtLeast(0), first)
}
