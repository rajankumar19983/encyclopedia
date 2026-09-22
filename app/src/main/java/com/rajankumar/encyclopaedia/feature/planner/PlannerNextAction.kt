package com.rajankumar.encyclopaedia.feature.planner

import com.rajankumar.encyclopaedia.data.local.PlannerTaskEntity

data class PlannerNextAction(val taskId: String, val title: String, val reason: String)

fun List<PlannerTaskEntity>.plannerNextAction(): PlannerNextAction? {
  val priority = plannerPriorities(limit = 1).firstOrNull() ?: return null
  return PlannerNextAction(priority.task.id, priority.task.title, priority.reason)
}
