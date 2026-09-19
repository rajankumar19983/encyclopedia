package com.rajankumar.encyclopaedia.feature.planner

fun List<PlannerDayHistory>.consistencyPercent(): Int {
  if (isEmpty()) return 0
  val completedDays = count { it.total > 0 && it.completed == it.total }
  return (completedDays * 100) / size
}
