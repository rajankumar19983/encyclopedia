package com.rajankumar.encyclopaedia.feature.planner

import com.rajankumar.encyclopaedia.data.local.PlannerTaskEntity

fun List<PlannerTaskEntity>.carriedCompletionPercent(): Int {
  val carried = filter { it.carriedFromDate != null }
  if (carried.isEmpty()) return 0
  return (carried.count { it.isCompleted } * 100) / carried.size
}
