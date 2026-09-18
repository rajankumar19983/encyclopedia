package com.rajankumar.encyclopaedia.feature.performance

data class PerformanceStat(val label: String, val value: String, val detail: String)

fun PerformanceSummary.stats(): List<PerformanceStat> = listOf(
  PerformanceStat("Accuracy", "$accuracy%", "${correct} of $attempts attempts correct"),
  PerformanceStat("Coverage", "$coverage%", "$uniqueQuestions of $totalQuestions questions practised"),
  PerformanceStat("Average time", formatDurationMs(averageTimeMs), "Average answer time"),
  PerformanceStat("Attempts", attempts.toString(), "$uniqueQuestions unique questions")
)
