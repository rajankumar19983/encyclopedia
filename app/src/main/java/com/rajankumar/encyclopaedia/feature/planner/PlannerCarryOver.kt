package com.rajankumar.encyclopaedia.feature.planner

import com.rajankumar.encyclopaedia.data.local.PlannerTaskEntity

fun carryIncompleteTasks(
  tasks: List<PlannerTaskEntity>,
  targetDate: String,
  now: Long = System.currentTimeMillis()
): List<PlannerTaskEntity> = tasks
  .filter { !it.isCompleted && it.scheduledDate < targetDate }
  .map { task ->
    task.copy(
      scheduledDate = targetDate,
      carriedFromDate = task.carriedFromDate ?: task.scheduledDate,
      updatedAt = now
    )
  }
