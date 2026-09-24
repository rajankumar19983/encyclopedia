package com.rajankumar.encyclopaedia.feature.planner

import com.rajankumar.encyclopaedia.data.local.PlannerTaskEntity

enum class PlannerBacklogHealth { CLEAR, MANAGEABLE, BUILDING, OVERLOADED }

fun List<PlannerTaskEntity>.backlogHealth(): PlannerBacklogHealth {
  val pending = count { !it.isCompleted }
  val carried = count { !it.isCompleted && it.carriedFromDate != null }
  return when {
    pending == 0 -> PlannerBacklogHealth.CLEAR
    pending <= 3 && carried <= 1 -> PlannerBacklogHealth.MANAGEABLE
    pending <= 6 && carried <= 3 -> PlannerBacklogHealth.BUILDING
    else -> PlannerBacklogHealth.OVERLOADED
  }
}

fun PlannerBacklogHealth.label(): String = name.lowercase().replaceFirstChar(Char::uppercase)
fun PlannerBacklogHealth.guidance(): String = when (this) {
  PlannerBacklogHealth.CLEAR -> "Plan a focused set of tasks for today."
  PlannerBacklogHealth.MANAGEABLE -> "Your pending work is manageable."
  PlannerBacklogHealth.BUILDING -> "Finish carried tasks before adding much more work."
  PlannerBacklogHealth.OVERLOADED -> "Reduce the backlog before planning new tasks."
}
