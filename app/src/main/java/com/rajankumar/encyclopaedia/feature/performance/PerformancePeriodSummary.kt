package com.rajankumar.encyclopaedia.feature.performance

import com.rajankumar.encyclopaedia.data.local.QuestionAttemptEntity

data class PerformancePeriodSummary(val period: PerformancePeriod, val attempts: Int, val correct: Int) {
  val accuracy get() = if (attempts == 0) 0 else correct * 100 / attempts
}

fun List<QuestionAttemptEntity>.periodSummary(period: PerformancePeriod): PerformancePeriodSummary {
  val filtered = withinPeriod(period)
  return PerformancePeriodSummary(period, filtered.size, filtered.count { it.isCorrect })
}
