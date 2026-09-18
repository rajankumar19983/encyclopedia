package com.rajankumar.encyclopaedia.feature.performance

import com.rajankumar.encyclopaedia.data.local.QuestionAttemptEntity
import java.time.Instant
import java.time.ZoneId

data class AccuracyTrend(val recent: Int, val previous: Int) {
  val change: Int get() = recent - previous
}

fun List<QuestionAttemptEntity>.accuracyTrend(window: Int = 20): AccuracyTrend {
  fun List<QuestionAttemptEntity>.accuracy() = if (isEmpty()) 0 else count { it.isCorrect } * 100 / size
  val ordered = sortedByDescending { it.attemptedAt }
  return AccuracyTrend(ordered.take(window).accuracy(), ordered.drop(window).take(window).accuracy())
}

data class ConsistencyStats(val activeDays: Int, val attemptsPerActiveDay: Float)

fun List<QuestionAttemptEntity>.consistencyStats(zoneId: ZoneId = ZoneId.systemDefault()): ConsistencyStats {
  val days = map { Instant.ofEpochMilli(it.attemptedAt).atZone(zoneId).toLocalDate() }.distinct().size
  return ConsistencyStats(days, if (days == 0) 0f else size.toFloat() / days)
}

data class AttemptBucket(val label: String, val attempts: Int, val correct: Int) {
  val accuracy: Int get() = if (attempts == 0) 0 else correct * 100 / attempts
}

fun List<QuestionAttemptEntity>.chunkedPerformance(size: Int = 10): List<AttemptBucket> =
  sortedBy { it.attemptedAt }.chunked(size.coerceAtLeast(1)).mapIndexed { index, group ->
    AttemptBucket("${index * size + 1}–${index * size + group.size}", group.size, group.count { it.isCorrect })
  }
