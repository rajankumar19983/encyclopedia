package com.rajankumar.encyclopaedia.feature.performance

import com.rajankumar.encyclopaedia.data.local.QuestionAttemptEntity

data class AccuracyTrend(val recent: Int, val previous: Int) {
  val change: Int get() = recent - previous
}

fun List<QuestionAttemptEntity>.accuracyTrend(window: Int = 20): AccuracyTrend {
  fun List<QuestionAttemptEntity>.accuracy() = if (isEmpty()) 0 else count { it.isCorrect } * 100 / size
  val ordered = sortedByDescending { it.attemptedAt }
  return AccuracyTrend(ordered.take(window).accuracy(), ordered.drop(window).take(window).accuracy())
}
