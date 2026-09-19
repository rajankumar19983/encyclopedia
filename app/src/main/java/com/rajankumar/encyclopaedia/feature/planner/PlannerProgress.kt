package com.rajankumar.encyclopaedia.feature.planner

import com.rajankumar.encyclopaedia.data.local.PlannerTaskEntity

data class PlannerProgress(
  val completed: Int,
  val total: Int
) {
  val remaining: Int get() = (total - completed).coerceAtLeast(0)
  val percent: Int get() = if (total == 0) 0 else (completed * 100) / total
}

fun List<PlannerTaskEntity>.plannerProgress(): PlannerProgress = PlannerProgress(
  completed = count { it.isCompleted },
  total = size
)
