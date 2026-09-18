package com.rajankumar.encyclopaedia.feature.performance

data class PerformanceDashboardState(
  val period: PerformancePeriod = PerformancePeriod.ALL,
  val weakQuestionQuery: String = "",
  val weakQuestionSort: PerformanceSort = PerformanceSort.MOST_MISTAKES
)

fun PerformanceData.visibleWeakQuestions(state: PerformanceDashboardState): List<WeakQuestion> =
  weakQuestions.searchWeakQuestions(state.weakQuestionQuery).sortedByPerformance(state.weakQuestionSort)
