package com.rajankumar.encyclopaedia.feature.performance

enum class PerformanceLevel(val label: String) {
  NO_DATA("No data"), BUILDING("Building"), DEVELOPING("Developing"), STRONG("Strong")
}

fun performanceLevel(attempts: Int, accuracy: Int): PerformanceLevel = when {
  attempts == 0 -> PerformanceLevel.NO_DATA
  attempts < 20 -> PerformanceLevel.BUILDING
  accuracy < 75 -> PerformanceLevel.DEVELOPING
  else -> PerformanceLevel.STRONG
}
