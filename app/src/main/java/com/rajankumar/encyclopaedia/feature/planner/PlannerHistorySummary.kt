package com.rajankumar.encyclopaedia.feature.planner

data class PlannerHistorySummary(
  val plannedDays: Int,
  val fullyCompletedDays: Int,
  val totalTasks: Int,
  val completedTasks: Int
) {
  val overallPercent: Int
    get() = if (totalTasks == 0) 0 else (completedTasks * 100) / totalTasks
}

fun List<PlannerDayHistory>.historySummary(): PlannerHistorySummary = PlannerHistorySummary(
  plannedDays = size,
  fullyCompletedDays = count { it.total > 0 && it.completed == it.total },
  totalTasks = sumOf { it.total },
  completedTasks = sumOf { it.completed }
)
