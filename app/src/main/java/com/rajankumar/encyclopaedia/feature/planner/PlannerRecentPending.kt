package com.rajankumar.encyclopaedia.feature.planner

fun List<PlannerDayHistory>.recentPendingTasks(days: Int = 7): Int {
  if (days <= 0) return 0
  return sortedByDescending { it.date }
    .take(days)
    .sumOf { (it.total - it.completed).coerceAtLeast(0) }
}
