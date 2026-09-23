package com.rajankumar.encyclopaedia.feature.performance

fun PerformanceEvidence.nextStep(): String = when {
  confidence == PerformanceConfidence.NONE -> "Start a practice session"
  accuracy.percent < 60 -> "Review incorrect answers"
  coverage.percent < 100 -> "Practise unseen questions"
  else -> "Use mixed revision to strengthen recall"
}
