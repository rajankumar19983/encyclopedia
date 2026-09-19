package com.rajankumar.encyclopaedia.feature.planner

fun List<PlannerDayHistory>.recentAveragePlanSize(days: Int = 7): Double {
  if (days <= 0) return 0.0
  val recent = sortedByDescending { it.date }.take(days)
  if (recent.isEmpty()) return 0.0
  return recent.sumOf { it.total }.toDouble() / recent.size
}
