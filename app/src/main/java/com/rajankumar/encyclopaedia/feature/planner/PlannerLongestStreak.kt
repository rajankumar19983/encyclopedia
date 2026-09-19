package com.rajankumar.encyclopaedia.feature.planner

import java.time.LocalDate

fun List<PlannerDayHistory>.longestCompletionStreak(): Int {
  val dates = filter { it.total > 0 && it.completed == it.total }
    .map { LocalDate.parse(it.date) }
    .sorted()
  if (dates.isEmpty()) return 0
  var longest = 1
  var current = 1
  for (index in 1 until dates.size) {
    if (dates[index - 1].plusDays(1) == dates[index]) current++ else current = 1
    if (current > longest) longest = current
  }
  return longest
}
