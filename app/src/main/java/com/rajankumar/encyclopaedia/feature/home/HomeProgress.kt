package com.rajankumar.encyclopaedia.feature.home

fun percentProgress(percent: Int): Float = percent.coerceIn(0, 100) / 100f

fun practiceProgress(attemptCount: Int, targetAttempts: Int = 20): Float {
  if (targetAttempts <= 0) return 0f
  return (attemptCount.coerceAtLeast(0) / targetAttempts.toFloat()).coerceIn(0f, 1f)
}
