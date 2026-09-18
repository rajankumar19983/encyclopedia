package com.rajankumar.encyclopaedia.feature.performance

data class PerformanceGoal(val targetAccuracy: Int = 80, val targetCoverage: Int = 100) {
  fun accuracyRemaining(current: Int) = (targetAccuracy.coerceIn(0, 100) - current).coerceAtLeast(0)
  fun coverageRemaining(current: Int) = (targetCoverage.coerceIn(0, 100) - current).coerceAtLeast(0)
}
