package com.rajankumar.encyclopaedia.feature.planner

import com.rajankumar.encyclopaedia.data.local.PlannerTaskEntity

data class PlannerProgress(val completed: Int, val total: Int) {
  val safeTotal: Int get() = total.coerceAtLeast(0)
  val safeCompleted: Int get() = completed.coerceIn(0, safeTotal)
  val remaining: Int get() = (safeTotal - safeCompleted).coerceAtLeast(0)
  val percent: Int get() = if (safeTotal == 0) 0 else (safeCompleted * 100) / safeTotal
  val isComplete: Boolean get() = safeTotal > 0 && safeCompleted == safeTotal
  val accessibilitySummary: String get() = if (safeTotal == 0) "No tasks planned today" else "$safeCompleted of $safeTotal tasks completed"
}

fun List<PlannerTaskEntity>.plannerProgress(): PlannerProgress = PlannerProgress(count { it.isCompleted }, size)
