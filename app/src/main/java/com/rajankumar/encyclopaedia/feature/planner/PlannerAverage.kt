package com.rajankumar.encyclopaedia.feature.planner

fun List<PlannerDayHistory>.averageTasksPerPlannedDay(): Double =
  if (isEmpty()) 0.0 else sumOf { it.total }.toDouble() / size
