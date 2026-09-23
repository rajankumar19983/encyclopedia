package com.rajankumar.encyclopaedia.feature.performance

import com.rajankumar.encyclopaedia.data.local.QuestionAttemptEntity
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId

data class StudyConsistency(
  val activeDays: Int,
  val currentStreak: Int,
  val longestStreak: Int,
)

fun List<QuestionAttemptEntity>.studyConsistency(
  today: LocalDate = LocalDate.now(),
  zoneId: ZoneId = ZoneId.systemDefault(),
): StudyConsistency {
  val days = map { Instant.ofEpochMilli(it.attemptedAt).atZone(zoneId).toLocalDate() }.distinct().sorted()
  if (days.isEmpty()) return StudyConsistency(0, 0, 0)
  var longest = 1
  var running = 1
  for (index in 1 until days.size) {
    running = if (days[index - 1].plusDays(1) == days[index]) running + 1 else 1
    longest = maxOf(longest, running)
  }
  val active = days.toSet()
  var cursor = if (today in active) today else today.minusDays(1)
  var current = 0
  while (cursor in active) {
    current++
    cursor = cursor.minusDays(1)
  }
  return StudyConsistency(days.size, current, longest)
}
