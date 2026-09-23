package com.rajankumar.encyclopaedia.feature.performance

import com.rajankumar.encyclopaedia.data.local.QuestionAttemptEntity
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId

data class DailyStudyActivity(
  val date: LocalDate,
  val attempts: Int,
  val correct: Int,
  val totalTimeMs: Long,
) {
  val accuracyPercent: Int get() = if (attempts == 0) 0 else correct * 100 / attempts
}

fun List<QuestionAttemptEntity>.dailyStudyActivity(
  zoneId: ZoneId = ZoneId.systemDefault(),
): List<DailyStudyActivity> = groupBy {
  Instant.ofEpochMilli(it.attemptedAt).atZone(zoneId).toLocalDate()
}.map { (date, attempts) ->
  DailyStudyActivity(
    date = date,
    attempts = attempts.size,
    correct = attempts.count { it.isCorrect },
    totalTimeMs = attempts.sumOf { it.timeTakenMs.coerceAtLeast(0) },
  )
}.sortedBy { it.date }
