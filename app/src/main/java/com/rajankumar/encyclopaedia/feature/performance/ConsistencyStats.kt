package com.rajankumar.encyclopaedia.feature.performance

import com.rajankumar.encyclopaedia.data.local.QuestionAttemptEntity
import java.time.Instant
import java.time.ZoneId

data class ConsistencyStats(val activeDays: Int, val attemptsPerActiveDay: Float)

fun List<QuestionAttemptEntity>.consistencyStats(zoneId: ZoneId = ZoneId.systemDefault()): ConsistencyStats {
  val days = map { Instant.ofEpochMilli(it.attemptedAt).atZone(zoneId).toLocalDate() }.distinct().size
  return ConsistencyStats(days, if (days == 0) 0f else size.toFloat() / days)
}
