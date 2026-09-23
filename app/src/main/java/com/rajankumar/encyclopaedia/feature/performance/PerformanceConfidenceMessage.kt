package com.rajankumar.encyclopaedia.feature.performance

fun PerformanceConfidence.message(): String = when (this) {
  PerformanceConfidence.LOW -> "Early estimate based on a limited practice history."
  PerformanceConfidence.MEDIUM -> "Growing evidence; more practice will make weak-area signals stronger."
  PerformanceConfidence.HIGH -> "Based on an established practice history."
}
