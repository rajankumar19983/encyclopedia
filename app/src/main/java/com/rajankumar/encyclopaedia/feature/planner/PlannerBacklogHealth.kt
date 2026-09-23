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

fun PlannerBacklogHealth.label(): String = when (this) {
  PlannerBacklogHealth.CLEAR -> "Clear"
  PlannerBacklogHealth.MANAGEABLE -> "Manageable"
  PlannerBacklogHealth.BUILDING -> "Building"
  PlannerBacklogHealth.OVERLOADED -> "Overloaded"
}
