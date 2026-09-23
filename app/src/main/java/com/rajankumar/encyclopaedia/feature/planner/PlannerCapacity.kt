package com.rajankumar.encyclopaedia.feature.planner

import com.rajankumar.encyclopaedia.data.local.PlannerTaskEntity

data class PlannerCapacity(val suggestedAdditionalTasks: Int, val message: String)

fun List<PlannerTaskEntity>.plannerCapacity(targetPending: Int = 5): PlannerCapacity {
  val pending = count { !it.isCompleted }
  val carried = count { !it.isCompleted && it.carriedFromDate != null }
  val room = (targetPending - pending).coerceAtLeast(0)
  val suggested = if (carried >= 3) 0 else room
  val message = when {
    suggested == 0 && pending > 0 -> "Focus on the current plan before adding more work."
    suggested == 0 -> "No additional tasks suggested."
    suggested == 1 -> "You have room for about 1 more task."
    else -> "You have room for about $suggested more tasks."
  }
  return PlannerCapacity(suggested, message)
}
