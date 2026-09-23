package com.rajankumar.encyclopaedia.feature.performance

fun PerformanceConfidence.message(): String = when (this) {
  PerformanceConfidence.NONE -> "No practice evidence yet. Complete a session to start analytics."
  PerformanceConfidence.LOW -> "Early estimate based on a small number of attempts."
  PerformanceConfidence.MODERATE -> "Useful estimate; more practice will make weak-area signals stronger."
  PerformanceConfidence.HIGH -> "Based on a substantial practice history."
}
