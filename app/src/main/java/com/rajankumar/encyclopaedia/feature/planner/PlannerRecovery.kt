package com.rajankumar.encyclopaedia.feature.planner

import com.rajankumar.encyclopaedia.data.local.PlannerTaskEntity

fun List<PlannerTaskEntity>.carriedCompletionPercent(): Int {
  val carried = filter { it.carriedFromDate != null }
  if (carried.isEmpty()) return 0
  return (carried.count { it.isCompleted } * 100) / carried.size
}

data class PlannerRecoveryInsight(val carriedCompletion: Int, val carryRate: Int, val message: String)

fun List<PlannerTaskEntity>.plannerRecoveryInsight(): PlannerRecoveryInsight {
  val recovery = carriedCompletionPercent()
  val carryRate = carryOverRatePercent()
  val message = when {
    none { it.carriedFromDate != null } -> "No carried-work history yet."
    recovery >= 75 -> "You usually recover carried tasks successfully."
    recovery < 40 -> "Carried tasks are often remaining unfinished. Keep new plans smaller until the backlog clears."
    else -> "Carried-work recovery is mixed. Prioritise the oldest pending tasks."
  }
  return PlannerRecoveryInsight(recovery, carryRate, message)
}
