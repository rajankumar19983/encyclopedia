package com.rajankumar.encyclopaedia.feature.planner

import com.rajankumar.encyclopaedia.data.local.PlannerTaskEntity

data class PlannerCarryOverSummary(
  val carriedCount: Int,
  val oldestSourceDate: String?
)

fun List<PlannerTaskEntity>.carryOverSummary(): PlannerCarryOverSummary {
  val carried = filter { !it.isCompleted && it.carriedFromDate != null }
  return PlannerCarryOverSummary(
    carriedCount = carried.size,
    oldestSourceDate = carried.mapNotNull { it.carriedFromDate }.minOrNull()
  )
}
