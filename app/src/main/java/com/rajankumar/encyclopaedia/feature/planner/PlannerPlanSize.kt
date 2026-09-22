package com.rajankumar.encyclopaedia.feature.planner

data class PlannerPlanSizeComparison(val recent: Double, val overall: Double) {
  fun message(): String = when {
    overall == 0.0 -> "No plan-size baseline yet."
    recent > overall + 1.0 -> "Recent plans are larger than your usual workload."
    recent + 1.0 < overall -> "Recent plans are lighter than your usual workload."
    else -> "Recent plan size is close to your usual workload."
  }
}

fun List<PlannerDayHistory>.planSizeComparison(): PlannerPlanSizeComparison = PlannerPlanSizeComparison(
  recent = recentAveragePlanSize(),
  overall = averageTasksPerPlannedDay(),
)
