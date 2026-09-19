package com.rajankumar.encyclopaedia.feature.planner

enum class PlannerTrend { IMPROVING, DECLINING, STEADY, INSUFFICIENT_DATA }

fun List<PlannerDayHistory>.completionTrend(window: Int = 3): PlannerTrend {
  if (window <= 0 || size < window * 2) return PlannerTrend.INSUFFICIENT_DATA
  val ordered = sortedByDescending { it.date }
  val recent = ordered.take(window).map { it.percent }.average()
  val previous = ordered.drop(window).take(window).map { it.percent }.average()
  return when {
    recent > previous -> PlannerTrend.IMPROVING
    recent < previous -> PlannerTrend.DECLINING
    else -> PlannerTrend.STEADY
  }
}
