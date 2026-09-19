package com.rajankumar.encyclopaedia.feature.performance

import com.rajankumar.encyclopaedia.data.local.QuestionAttemptEntity

data class SpeedTrend(val recentAverageMs: Long, val previousAverageMs: Long) {
  val improvementMs: Long get() = previousAverageMs - recentAverageMs
}

fun List<QuestionAttemptEntity>.speedTrend(window: Int = 20): SpeedTrend {
  fun List<QuestionAttemptEntity>.avg() = if (isEmpty()) 0 else sumOf { it.timeTakenMs.coerceAtLeast(0) } / size
  val ordered = sortedByDescending { it.attemptedAt }
  return SpeedTrend(ordered.take(window).avg(), ordered.drop(window).take(window).avg())
}
