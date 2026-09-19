package com.rajankumar.encyclopaedia.feature.planner

data class PlannerTaskBalance(val completed: Int, val pending: Int) {
  val completionPercent: Int get() = if (completed + pending == 0) 0 else (completed * 100) / (completed + pending)
}

fun List<PlannerDayHistory>.taskBalance(): PlannerTaskBalance = PlannerTaskBalance(
  completed = sumOf { it.completed },
  pending = sumOf { it.total - it.completed }
)
