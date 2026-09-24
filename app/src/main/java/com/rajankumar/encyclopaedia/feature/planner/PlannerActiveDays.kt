package com.rajankumar.encyclopaedia.feature.planner

import java.time.LocalDate
import java.time.temporal.ChronoUnit

fun List<PlannerDayHistory>.activeDaySpan(): Long {
  val dates = mapNotNull { runCatching { LocalDate.parse(it.date) }.getOrNull() }
  if (dates.isEmpty()) return 0
  return ChronoUnit.DAYS.between(dates.minOrNull(), dates.maxOrNull()) + 1
}
