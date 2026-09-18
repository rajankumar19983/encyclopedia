package com.rajankumar.encyclopaedia.feature.questions

import com.rajankumar.encyclopaedia.data.local.QuestionAttemptEntity
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId

fun List<QuestionAttemptEntity>.calculateStudyStreak(
  today: LocalDate = LocalDate.now(),
  zoneId: ZoneId = ZoneId.systemDefault()
): Int {
  val activeDates = map { Instant.ofEpochMilli(it.attemptedAt).atZone(zoneId).toLocalDate() }.toSet()
  var date = if (today in activeDates) today else today.minusDays(1)
  if (date !in activeDates) return 0
  var streak = 0
  while (date in activeDates) {
    streak++
    date = date.minusDays(1)
  }
  return streak
}
