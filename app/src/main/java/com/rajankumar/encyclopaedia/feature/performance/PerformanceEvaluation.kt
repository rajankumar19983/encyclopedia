package com.rajankumar.encyclopaedia.feature.performance

data class PerformanceComparison(val accuracyChange: Int, val speedImprovementMs: Long) {
  val accuracyImproved get() = accuracyChange > 0
  val speedImproved get() = speedImprovementMs > 0
}

fun PerformanceData.comparison() = PerformanceComparison(trend.change, speedTrend.improvementMs)

data class PerformanceGoal(val targetAccuracy: Int = 80, val targetCoverage: Int = 100) {
  fun accuracyRemaining(current: Int) = (targetAccuracy.coerceIn(0, 100) - current).coerceAtLeast(0)
  fun coverageRemaining(current: Int) = (targetCoverage.coerceIn(0, 100) - current).coerceAtLeast(0)
}

data class PerformanceDataQuality(val confidence: PerformanceConfidence, val enoughForTrend: Boolean)

fun PerformanceSummary.dataQuality() = PerformanceDataQuality(performanceConfidence(attempts), attempts >= 20)
