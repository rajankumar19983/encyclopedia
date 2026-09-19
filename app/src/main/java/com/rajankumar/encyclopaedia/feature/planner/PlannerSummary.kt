package com.rajankumar.encyclopaedia.feature.planner

fun PlannerProgress.summaryText(): String = when {
  total == 0 -> "Plan your first study task for today."
  completed == total -> "Today's plan is complete."
  completed == 0 -> "$total tasks ready to start."
  else -> "$remaining tasks left for today."
}
