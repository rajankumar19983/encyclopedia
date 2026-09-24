package com.rajankumar.encyclopaedia.feature.revision

fun revisionAccuracyPercent(correct: Int, attempts: Int): Int {
  val safeAttempts = attempts.coerceAtLeast(0)
  if (safeAttempts == 0) return 0
  return (correct.coerceIn(0, safeAttempts) * 100 / safeAttempts).coerceIn(0, 100)
}
