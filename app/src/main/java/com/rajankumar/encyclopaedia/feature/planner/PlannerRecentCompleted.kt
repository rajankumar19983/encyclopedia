package com.rajankumar.encyclopaedia.feature.planner

fun List<PlannerDayHistory>.recentCompletedTasks(days: Int = 7): Int {
  if (days <= 0) return 0
  return sortedByDescending { it.date }.take(days).sumOf { it.completed }
}
