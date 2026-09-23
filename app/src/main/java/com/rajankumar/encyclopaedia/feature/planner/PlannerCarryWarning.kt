package com.rajankumar.encyclopaedia.feature.planner

import com.rajankumar.encyclopaedia.data.local.PlannerTaskEntity

data class PlannerCarryWarning(val count: Int, val message: String)

fun List<PlannerTaskEntity>.plannerCarryWarning(): PlannerCarryWarning? {
  val carried = count { !it.isCompleted && it.carriedFromDate != null }
  if (carried < 3) return null
  val message = if (carried >= 6) {
    "$carried carried tasks are competing with today's plan. Reduce new work and clear the backlog first."
  } else {
    "$carried carried tasks remain. Finish older work before expanding today's plan."
  }
  return PlannerCarryWarning(carried, message)
}
