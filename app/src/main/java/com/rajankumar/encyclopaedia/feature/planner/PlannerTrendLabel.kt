package com.rajankumar.encyclopaedia.feature.planner

fun PlannerTrend.displayText(): String = when (this) {
  PlannerTrend.IMPROVING -> "Improving"
  PlannerTrend.DECLINING -> "Needs attention"
  PlannerTrend.STEADY -> "Steady"
  PlannerTrend.INSUFFICIENT_DATA -> "Not enough history yet"
}
