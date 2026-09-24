package com.rajankumar.encyclopaedia.feature.home

import com.rajankumar.encyclopaedia.data.local.PlannerTaskEntity

data class HomeTodayPlan(val completed: Int, val total: Int, val nextTask: String?) {
  val progress: Float get() = if (total == 0) 0f else completed.toFloat() / total
  val summary: String get() = if (total == 0) "No tasks planned for today yet." else "$completed of $total tasks completed"
}

fun List<PlannerTaskEntity>.toHomeTodayPlan(): HomeTodayPlan = HomeTodayPlan(
  completed = count { it.isCompleted },
  total = size,
  nextTask = firstOrNull { !it.isCompleted }?.title
)
