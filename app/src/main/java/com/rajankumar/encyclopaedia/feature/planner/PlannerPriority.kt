package com.rajankumar.encyclopaedia.feature.planner

import com.rajankumar.encyclopaedia.data.local.PlannerTaskEntity

data class PlannerPriorityItem(val task: PlannerTaskEntity, val rank: Int, val reason: String)

fun List<PlannerTaskEntity>.plannerPriorities(limit: Int = 3): List<PlannerPriorityItem> =
  filterNot { it.isCompleted }
    .sortedWith(compareBy<PlannerTaskEntity>({ it.carriedFromDate == null }, { it.carriedFromDate ?: it.scheduledDate }, { it.createdAt }, { it.id }))
    .take(limit.coerceAtLeast(0))
    .mapIndexed { index, task ->
      PlannerPriorityItem(
        task = task,
        rank = index + 1,
        reason = if (task.carriedFromDate != null) "Carried from ${plannerDisplayDate(task.carriedFromDate)}" else "Planned for today",
      )
    }
