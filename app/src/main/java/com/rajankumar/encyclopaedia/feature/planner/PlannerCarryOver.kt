package com.rajankumar.encyclopaedia.feature.planner

import com.rajankumar.encyclopaedia.data.local.PlannerTaskEntity

fun carryIncompleteTasks(tasks: List<PlannerTaskEntity>, targetDate: String, now: Long = System.currentTimeMillis()): List<PlannerTaskEntity> {
  val cleanTarget = targetDate.trim()
  if (cleanTarget.isEmpty()) return emptyList()
  return tasks
    .filter { !it.isCompleted && it.scheduledDate.isNotBlank() && it.scheduledDate < cleanTarget }
    .map { task -> task.copy(scheduledDate = cleanTarget, carriedFromDate = task.carriedFromDate ?: task.scheduledDate, updatedAt = now) }
}
