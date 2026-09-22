package com.rajankumar.encyclopaedia.feature.planner

import com.rajankumar.encyclopaedia.data.local.PlannerTaskEntity

enum class PlannerDayStatus { UNPLANNED, NOT_STARTED, IN_PROGRESS, COMPLETE }

fun List<PlannerTaskEntity>.plannerDayStatus(): PlannerDayStatus {
  if (isEmpty()) return PlannerDayStatus.UNPLANNED
  val completed = count { it.isCompleted }
  return when {
    completed == 0 -> PlannerDayStatus.NOT_STARTED
    completed == size -> PlannerDayStatus.COMPLETE
    else -> PlannerDayStatus.IN_PROGRESS
  }
}

fun PlannerDayStatus.displayText(): String = when (this) {
  PlannerDayStatus.UNPLANNED -> "Not planned"
  PlannerDayStatus.NOT_STARTED -> "Ready to start"
  PlannerDayStatus.IN_PROGRESS -> "In progress"
  PlannerDayStatus.COMPLETE -> "Complete"
}
