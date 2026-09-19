package com.rajankumar.encyclopaedia.feature.planner

data class PlannerCompletionRange(val lowestPercent: Int, val highestPercent: Int)

fun List<PlannerDayHistory>.completionRange(): PlannerCompletionRange {
  if (isEmpty()) return PlannerCompletionRange(0, 0)
  return PlannerCompletionRange(
    lowestPercent = minOf { it.percent },
    highestPercent = maxOf { it.percent }
  )
}
