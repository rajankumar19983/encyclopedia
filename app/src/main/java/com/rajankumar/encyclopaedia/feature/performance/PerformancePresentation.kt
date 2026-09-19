package com.rajankumar.encyclopaedia.feature.performance

object PerformanceConstants {
  const val trendWindow = 20
  const val targetAccuracy = 80
  const val minimumTrendAttempts = 20
}

const val performanceEmptyMessage = "Complete practice questions to build accuracy, speed, coverage and weak-area insights."

fun formatDurationMs(milliseconds: Long): String {
  val seconds = milliseconds.coerceAtLeast(0) / 1000
  return when {
    seconds < 60 -> "${seconds}s"
    else -> "${seconds / 60}m ${seconds % 60}s"
  }
}

fun signedPercent(value: Int): String = when {
  value > 0 -> "+$value%"
  else -> "$value%"
}
