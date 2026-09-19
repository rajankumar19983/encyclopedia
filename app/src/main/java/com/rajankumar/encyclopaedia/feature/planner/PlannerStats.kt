package com.rajankumar.encyclopaedia.feature.planner

import com.rajankumar.encyclopaedia.data.local.PlannerTaskEntity

data class PlannerCompletionStats(
  val completedToday: Int,
  val carriedPending: Int,
  val newlyPlannedPending: Int
)

fun List<PlannerTaskEntity>.completionStats(): PlannerCompletionStats = PlannerCompletionStats(
  completedToday = count { it.isCompleted },
  carriedPending = count { !it.isCompleted && it.carriedFromDate != null },
  newlyPlannedPending = count { !it.isCompleted && it.carriedFromDate == null }
)
