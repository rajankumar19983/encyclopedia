package com.rajankumar.encyclopaedia.feature.performance

import java.time.LocalDate

data class WeeklyStudySummary(
  val activeDays: Int,
  val attempts: Int,
  val correct: Int,
  val totalTimeMs: Long,
) {
  val accuracyPercent: Int get() = if (attempts == 0) 0 else correct * 100 / attempts
}

fun List<DailyStudyActivity>.weeklySummary(
  today: LocalDate = LocalDate.now(),
): WeeklyStudySummary {
  val start = today.minusDays(6)
  val days = filter { !it.date.isBefore(start) && !it.date.isAfter(today) }
  return WeeklyStudySummary(
    activeDays = days.size,
    attempts = days.sumOf { it.attempts },
    correct = days.sumOf { it.correct },
    totalTimeMs = days.sumOf { it.totalTimeMs },
  )
}
