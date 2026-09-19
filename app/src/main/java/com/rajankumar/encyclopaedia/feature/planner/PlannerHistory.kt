package com.rajankumar.encyclopaedia.feature.planner

import com.rajankumar.encyclopaedia.data.local.PlannerTaskEntity

data class PlannerDayHistory(
  val date: String,
  val completed: Int,
  val total: Int
) {
  val percent: Int get() = if (total == 0) 0 else (completed * 100) / total
}

fun List<PlannerTaskEntity>.plannerHistory(): List<PlannerDayHistory> =
  groupBy { it.scheduledDate }
    .map { (date, tasks) ->
      PlannerDayHistory(
        date = date,
        completed = tasks.count { it.isCompleted },
        total = tasks.size
      )
    }
    .sortedByDescending { it.date }
