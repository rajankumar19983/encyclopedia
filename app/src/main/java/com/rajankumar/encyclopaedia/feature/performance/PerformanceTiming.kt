package com.rajankumar.encyclopaedia.feature.performance

fun PerformanceSummary.averageTimeText(): String {
  val seconds = averageTimeMs.coerceAtLeast(0) / 1_000
  return if (seconds == 0L) "No timing data" else if (seconds < 60) "${seconds}s average" else "${seconds / 60}m ${seconds % 60}s average"
}
