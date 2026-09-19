package com.rajankumar.encyclopaedia.feature.planner

import java.time.LocalDate
import java.time.temporal.ChronoUnit

fun List<PlannerDayHistory>.activeDaySpan(): Long {
  if (isEmpty()) return 0
  val dates = map { LocalDate.parse(it.date) }
  return ChronoUnit.DAYS.between(dates.minOrNull(), dates.maxOrNull()) + 1
}
