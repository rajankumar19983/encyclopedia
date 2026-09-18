package com.rajankumar.encyclopaedia.feature.performance

data class PerformanceMilestone(val title: String, val reached: Boolean)

fun PerformanceSummary.milestones(): List<PerformanceMilestone> = listOf(
  PerformanceMilestone("First 10 attempts", attempts >= 10),
  PerformanceMilestone("50 attempts", attempts >= 50),
  PerformanceMilestone("80% accuracy", attempts >= 10 && accuracy >= 80),
  PerformanceMilestone("Full question coverage", totalQuestions > 0 && coverage >= 100)
)
