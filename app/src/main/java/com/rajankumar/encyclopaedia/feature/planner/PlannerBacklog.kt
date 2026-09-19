package com.rajankumar.encyclopaedia.feature.planner

import com.rajankumar.encyclopaedia.data.local.PlannerTaskEntity

fun List<PlannerTaskEntity>.backlogPressurePercent(): Int {
  val pending = filter { !it.isCompleted }
  if (pending.isEmpty()) return 0
  return (pending.count { it.carriedFromDate != null } * 100) / pending.size
}
