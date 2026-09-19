package com.rajankumar.encyclopaedia.feature.planner

import com.rajankumar.encyclopaedia.data.local.PlannerTaskEntity

fun PlannerTaskEntity.withCompletion(
  completed: Boolean,
  now: Long = System.currentTimeMillis()
): PlannerTaskEntity = copy(
  isCompleted = completed,
  completedAt = if (completed) now else null,
  updatedAt = now
)

fun List<PlannerTaskEntity>.allPlannerTasksComplete(): Boolean = isNotEmpty() && all { it.isCompleted }
