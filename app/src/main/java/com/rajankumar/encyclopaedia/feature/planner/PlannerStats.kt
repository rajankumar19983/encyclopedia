package com.rajankumar.encyclopaedia.feature.planner

import com.rajankumar.encyclopaedia.data.local.PlannerTaskEntity

data class PlannerCompletionStats(val completedToday: Int, val carriedPending: Int, val newlyPlannedPending: Int) {
  val pending: Int get() = carriedPending + newlyPlannedPending
  val total: Int get() = completedToday + pending
  val summary: String get() = "Completed $completedToday • Pending $pending"
}

fun List<PlannerTaskEntity>.completionStats(): PlannerCompletionStats = PlannerCompletionStats(
  completedToday = count { it.isCompleted },
  carriedPending = count { !it.isCompleted && it.carriedFromDate != null },
  newlyPlannedPending = count { !it.isCompleted && it.carriedFromDate == null }
)
