package com.rajankumar.encyclopaedia.feature.planner

import com.rajankumar.encyclopaedia.data.local.PlannerTaskEntity

fun List<PlannerTaskEntity>.backlogPressurePercent(): Int {
  val pending = filter { !it.isCompleted }
  if (pending.isEmpty()) return 0
  return (pending.count { it.carriedFromDate != null } * 100 / pending.size).coerceIn(0, 100)
}

fun backlogPressureLabel(percent: Int): String = when (percent.coerceIn(0, 100)) {
  0 -> "No carried backlog"
  in 1..39 -> "Light carried backlog"
  in 40..69 -> "Moderate carried backlog"
  else -> "High carried backlog"
}
