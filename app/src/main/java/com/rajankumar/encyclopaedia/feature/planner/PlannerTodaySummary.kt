package com.rajankumar.encyclopaedia.feature.planner

import com.rajankumar.encyclopaedia.data.local.PlannerTaskEntity

data class PlannerTodaySummary(
  val status: PlannerDayStatus,
  val focus: PlannerFocus,
  val workload: PlannerWorkload,
  val nextAction: PlannerNextAction?,
) {
  fun supportingText(): String = when (status) {
    PlannerDayStatus.UNPLANNED -> workload.guidance()
    PlannerDayStatus.COMPLETE -> "Everything planned for today is complete."
    else -> nextAction?.let { "Next: ${it.title}" } ?: workload.guidance()
  }
}

fun List<PlannerTaskEntity>.plannerTodaySummary() = PlannerTodaySummary(
  status = plannerDayStatus(),
  focus = plannerFocus(),
  workload = plannerWorkload(),
  nextAction = plannerNextAction(),
)
