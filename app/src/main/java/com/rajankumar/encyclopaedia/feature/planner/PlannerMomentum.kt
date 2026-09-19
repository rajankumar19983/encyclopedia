package com.rajankumar.encyclopaedia.feature.planner

fun List<PlannerDayHistory>.recentCompletionPercent(days: Int = 7): Int {
  if (days <= 0) return 0
  val recent = sortedByDescending { it.date }.take(days)
  val total = recent.sumOf { it.total }
  if (total == 0) return 0
  return (recent.sumOf { it.completed } * 100) / total
}
