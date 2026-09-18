package com.rajankumar.encyclopaedia.feature.questions

import com.rajankumar.encyclopaedia.data.local.QuestionAttemptEntity
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId

data class DailyPracticeStats(val date: LocalDate, val attempts: Int, val correct: Int) {
  val accuracy: Int get() = accuracyPercent(correct, attempts)
}

fun List<QuestionAttemptEntity>.dailyPracticeStats(zoneId: ZoneId = ZoneId.systemDefault()): List<DailyPracticeStats> =
  groupBy { Instant.ofEpochMilli(it.attemptedAt).atZone(zoneId).toLocalDate() }
    .map { (date, attempts) -> DailyPracticeStats(date, attempts.size, attempts.count { it.isCorrect }) }
    .sortedByDescending { it.date }
