package com.rajankumar.encyclopaedia.feature.performance

enum class AccuracyBand(val label: String) {
  NEEDS_WORK("Needs work"),
  DEVELOPING("Developing"),
  GOOD("Good"),
  EXCELLENT("Excellent")
}

fun accuracyBand(accuracy: Int): AccuracyBand = when (accuracy.coerceIn(0, 100)) {
  in 0..49 -> AccuracyBand.NEEDS_WORK
  in 50..69 -> AccuracyBand.DEVELOPING
  in 70..84 -> AccuracyBand.GOOD
  else -> AccuracyBand.EXCELLENT
}

enum class CoverageBand(val label: String) {
  STARTING("Starting"),
  PARTIAL("Partial"),
  BROAD("Broad"),
  COMPLETE("Complete")
}

fun coverageBand(coverage: Int): CoverageBand = when (coverage.coerceIn(0, 100)) {
  in 0..24 -> CoverageBand.STARTING
  in 25..59 -> CoverageBand.PARTIAL
  in 60..99 -> CoverageBand.BROAD
  else -> CoverageBand.COMPLETE
}

enum class PerformanceConfidence(val label: String) {
  LOW("Limited data"),
  MEDIUM("Growing data"),
  HIGH("Established data")
}

fun performanceConfidence(attempts: Int): PerformanceConfidence = when {
  attempts < 20 -> PerformanceConfidence.LOW
  attempts < 100 -> PerformanceConfidence.MEDIUM
  else -> PerformanceConfidence.HIGH
}
