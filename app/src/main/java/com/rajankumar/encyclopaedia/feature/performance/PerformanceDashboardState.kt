package com.rajankumar.encyclopaedia.feature.performance

data class PerformanceDashboardState(
  val period: PerformancePeriod = PerformancePeriod.ALL,
  val weakQuestionQuery: String = "",
  val weakQuestionSort: PerformanceSort = PerformanceSort.MOST_MISTAKES
)
