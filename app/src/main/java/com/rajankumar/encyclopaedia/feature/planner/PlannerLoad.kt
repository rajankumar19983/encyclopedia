package com.rajankumar.encyclopaedia.feature.planner

import com.rajankumar.encyclopaedia.data.local.PlannerTaskEntity

data class PlannerPendingLoad(
  val totalPending: Int,
  val carriedPending: Int,
  val newPending: Int
)

fun List<PlannerTaskEntity>.pendingLoad(): PlannerPendingLoad = PlannerPendingLoad(
  totalPending = count { !it.isCompleted },
  carriedPending = count { !it.isCompleted && it.carriedFromDate != null },
  newPending = count { !it.isCompleted && it.carriedFromDate == null }
)
