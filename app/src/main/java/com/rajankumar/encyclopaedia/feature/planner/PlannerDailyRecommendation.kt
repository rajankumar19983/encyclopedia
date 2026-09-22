package com.rajankumar.encyclopaedia.feature.planner

import com.rajankumar.encyclopaedia.data.local.PlannerTaskEntity

data class PlannerDailyRecommendation(val title: String, val detail: String)

fun List<PlannerTaskEntity>.dailyPlannerRecommendation(): PlannerDailyRecommendation {
  val focus = plannerFocus()
  val workload = plannerWorkload()
  return when {
    isEmpty() -> PlannerDailyRecommendation("Build today's plan", workload.guidance())
    focus.pending == 0 -> PlannerDailyRecommendation("Plan completed", "All ${focus.total} planned tasks are complete.")
    focus.carriedPending > 0 -> PlannerDailyRecommendation("Clear carried work first", "${focus.carriedPending} carried task(s) remain. ${workload.guidance()}")
    else -> PlannerDailyRecommendation("Continue today's plan", workload.guidance())
  }
}
