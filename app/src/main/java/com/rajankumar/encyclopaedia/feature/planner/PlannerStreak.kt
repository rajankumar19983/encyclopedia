package com.rajankumar.encyclopaedia.feature.planner

import java.time.LocalDate

fun List<PlannerDayHistory>.completionStreak(referenceDate: String): Int {
  val completedDates = filter { it.total > 0 && it.completed == it.total }
    .map { LocalDate.parse(it.date) }
    .toSet()
  var date = LocalDate.parse(referenceDate)
  var streak = 0
  while (date in completedDates) {
    streak++
    date = date.minusDays(1)
  }
  return streak
}
