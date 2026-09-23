package com.rajankumar.encyclopaedia.feature.planner

import com.rajankumar.encyclopaedia.data.local.PlannerTaskEntity

enum class PlannerWorkloadLevel { EMPTY, LIGHT, BALANCED, HEAVY }

data class PlannerWorkload(val level: PlannerWorkloadLevel, val pending: Int, val carried: Int) {
  fun guidance(): String = when (level) {
    PlannerWorkloadLevel.EMPTY -> "Add a realistic study target for today."
    PlannerWorkloadLevel.LIGHT -> "A light plan leaves room for focused revision."
    PlannerWorkloadLevel.BALANCED -> "Your pending workload looks manageable."
    PlannerWorkloadLevel.HEAVY -> if (carried > 0) "Prioritise carried work before adding more tasks." else "Consider finishing the highest-value tasks first."
  }
}

fun List<PlannerTaskEntity>.plannerWorkload(): PlannerWorkload {
  val pending = count { !it.isCompleted }
  val carried = count { !it.isCompleted && it.carriedFromDate != null }
  val level = when {
    pending == 0 && isEmpty() -> PlannerWorkloadLevel.EMPTY
    pending <= 2 -> PlannerWorkloadLevel.LIGHT
    pending <= 5 -> PlannerWorkloadLevel.BALANCED
    else -> PlannerWorkloadLevel.HEAVY
  }
  return PlannerWorkload(level, pending, carried)
}
