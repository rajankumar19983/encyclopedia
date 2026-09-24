package com.rajankumar.encyclopaedia.feature.planner

import com.rajankumar.encyclopaedia.data.local.PlannerTaskEntity

data class PlannerCarryOverSummary(val carriedCount: Int, val oldestSourceDate: String?) {
  val hasBacklog: Boolean get() = carriedCount > 0
  val summary: String get() = when (carriedCount) {
    0 -> "No carried-over tasks"
    1 -> "1 carried-over task"
    else -> "$carriedCount carried-over tasks"
  }
}

fun List<PlannerTaskEntity>.carryOverSummary(): PlannerCarryOverSummary {
  val carried = filter { !it.isCompleted && it.carriedFromDate != null }
  return PlannerCarryOverSummary(carried.size, carried.mapNotNull { it.carriedFromDate }.minOrNull())
}
