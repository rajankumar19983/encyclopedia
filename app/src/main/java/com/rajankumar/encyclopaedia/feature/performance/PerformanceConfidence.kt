package com.rajankumar.encyclopaedia.feature.performance

enum class PerformanceConfidence(val label: String) { LOW("Limited data"), MEDIUM("Growing data"), HIGH("Established data") }

fun performanceConfidence(attempts: Int): PerformanceConfidence = when {
  attempts < 20 -> PerformanceConfidence.LOW
  attempts < 100 -> PerformanceConfidence.MEDIUM
  else -> PerformanceConfidence.HIGH
}
