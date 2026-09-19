package com.rajankumar.encyclopaedia.feature.planner

import com.rajankumar.encyclopaedia.data.local.PlannerTaskEntity

fun List<PlannerTaskEntity>.orderedForPlanner(): List<PlannerTaskEntity> = sortedWith(
  compareBy<PlannerTaskEntity> { it.isCompleted }
    .thenBy { it.carriedFromDate == null }
    .thenBy { it.createdAt }
)
