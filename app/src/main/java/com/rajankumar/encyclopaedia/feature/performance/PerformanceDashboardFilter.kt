package com.rajankumar.encyclopaedia.feature.performance

fun PerformanceData.visibleWeakQuestions(state: PerformanceDashboardState): List<WeakQuestion> =
  weakQuestions.searchWeakQuestions(state.weakQuestionQuery).sortedByPerformance(state.weakQuestionSort)
