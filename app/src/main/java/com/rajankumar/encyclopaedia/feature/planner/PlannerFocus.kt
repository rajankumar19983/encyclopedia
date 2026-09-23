package com.rajankumar.encyclopaedia.feature.planner

import com.rajankumar.encyclopaedia.data.local.PlannerTaskEntity

data class PlannerFocus(
  val total: Int,
  val completed: Int,
  val pending: Int,
  val carriedPending: Int,
) {
  val newPending: Int get() = (pending - carriedPending).coerceAtLeast(0)
  val completionPercent: Int get() = if (total == 0) 0 else completed * 100 / total

  fun headline(): String = when {
    total == 0 -> "Plan your first study task"
    pending == 0 -> "Today's plan is complete"
    carriedPending > 0 -> "$pending tasks left • $carriedPending carried"
    else -> "$pending tasks left today"
  }
}

fun List<PlannerTaskEntity>.plannerFocus(): PlannerFocus = PlannerFocus(
  total = size,
  completed = count { it.isCompleted },
  pending = count { !it.isCompleted },
  carriedPending = count { !it.isCompleted && it.carriedFromDate != null },
)
