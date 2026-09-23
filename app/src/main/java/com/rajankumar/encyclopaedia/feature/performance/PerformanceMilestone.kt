package com.rajankumar.encyclopaedia.feature.performance

data class PerformanceMilestone(
  val accuracy: Boolean,
  val coverage: Boolean,
  val trends: Boolean,
  val weakQuestions: Boolean,
  val topicBreakdown: Boolean,
  val consistency: Boolean,
  val evidenceConfidence: Boolean,
  val actionableGuidance: Boolean,
) {
  val complete: Boolean get() = accuracy && coverage && trends && weakQuestions && topicBreakdown && consistency && evidenceConfidence && actionableGuidance
}

val currentPerformanceMilestone = PerformanceMilestone(true, true, true, true, true, true, true, true)
