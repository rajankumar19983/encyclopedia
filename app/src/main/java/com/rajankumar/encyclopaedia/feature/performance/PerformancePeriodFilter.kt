package com.rajankumar.encyclopaedia.feature.performance

import com.rajankumar.encyclopaedia.data.local.QuestionAttemptEntity

fun List<QuestionAttemptEntity>.withinPeriod(period: PerformancePeriod, now: Long = System.currentTimeMillis()): List<QuestionAttemptEntity> {
  if (period == PerformancePeriod.ALL) return this
  val cutoff = now - period.days * 86_400_000L
  return filter { it.attemptedAt >= cutoff }
}
