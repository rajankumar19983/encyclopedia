package com.rajankumar.encyclopaedia.feature.performance

data class PerformanceEvidence(
  val accuracy: AccuracySample,
  val coverage: PerformanceCoverage,
  val confidence: PerformanceConfidence,
)

fun performanceEvidence(totalQuestions: Int, practisedQuestions: Int, attempts: Int, correct: Int) = PerformanceEvidence(
  AccuracySample(correct, attempts),
  PerformanceCoverage(practisedQuestions, totalQuestions),
  performanceConfidence(attempts),
)
