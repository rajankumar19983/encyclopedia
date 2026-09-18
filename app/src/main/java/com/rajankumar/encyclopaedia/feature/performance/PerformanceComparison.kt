package com.rajankumar.encyclopaedia.feature.performance

data class PerformanceComparison(val accuracyChange: Int, val speedImprovementMs: Long) {
  val accuracyImproved get() = accuracyChange > 0
  val speedImproved get() = speedImprovementMs > 0
}

fun PerformanceData.comparison() = PerformanceComparison(trend.change, speedTrend.improvementMs)
