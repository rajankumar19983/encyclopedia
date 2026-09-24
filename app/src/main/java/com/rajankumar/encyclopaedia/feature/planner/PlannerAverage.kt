package com.rajankumar.encyclopaedia.feature.planner

fun List<PlannerDayHistory>.averageTasksPerPlannedDay(): Double {
  if (isEmpty()) return 0.0
  return sumOf { it.total.coerceAtLeast(0) }.toDouble() / size
}
