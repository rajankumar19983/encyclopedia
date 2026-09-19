package com.rajankumar.encyclopaedia.feature.planner

fun List<PlannerDayHistory>.recentPerfectDayPercent(days: Int = 7): Int {
  if (days <= 0) return 0
  val recent = sortedByDescending { it.date }.take(days)
  if (recent.isEmpty()) return 0
  return (recent.count { it.total > 0 && it.completed == it.total } * 100) / recent.size
}
