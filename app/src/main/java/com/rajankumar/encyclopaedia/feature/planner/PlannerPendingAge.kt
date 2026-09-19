package com.rajankumar.encyclopaedia.feature.planner

import com.rajankumar.encyclopaedia.data.local.PlannerTaskEntity
import java.time.LocalDate
import java.time.temporal.ChronoUnit

fun List<PlannerTaskEntity>.oldestPendingAgeDays(today: String): Long {
  val current = LocalDate.parse(today)
  val oldest = filter { !it.isCompleted }
    .map { LocalDate.parse(it.carriedFromDate ?: it.scheduledDate) }
    .minOrNull() ?: return 0
  return ChronoUnit.DAYS.between(oldest, current).coerceAtLeast(0)
}
