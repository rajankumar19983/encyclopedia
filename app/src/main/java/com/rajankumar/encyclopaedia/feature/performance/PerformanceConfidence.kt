package com.rajankumar.encyclopaedia.feature.performance

enum class PerformanceConfidence { NONE, LOW, MODERATE, HIGH }

fun performanceConfidence(attemptCount: Int): PerformanceConfidence = when {
  attemptCount <= 0 -> PerformanceConfidence.NONE
  attemptCount < 10 -> PerformanceConfidence.LOW
  attemptCount < 30 -> PerformanceConfidence.MODERATE
  else -> PerformanceConfidence.HIGH
}
