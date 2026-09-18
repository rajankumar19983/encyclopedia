package com.rajankumar.encyclopaedia.feature.performance

fun PerformanceData.narrative(): String {
  if (summary.attempts == 0) return performanceEmptyMessage
  val trendText = trendLabel(trend.change).lowercase()
  return "${summary.accuracy}% accuracy across ${summary.attempts} attempts, with ${summary.coverage}% question coverage. Recent accuracy is $trendText."
}
