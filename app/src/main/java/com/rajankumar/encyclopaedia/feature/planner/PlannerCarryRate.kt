package com.rajankumar.encyclopaedia.feature.planner

import com.rajankumar.encyclopaedia.data.local.PlannerTaskEntity

fun List<PlannerTaskEntity>.carryOverRatePercent(): Int = if (isEmpty()) 0 else (count { it.carriedFromDate != null } * 100 / size).coerceIn(0, 100)
fun carryOverRateLabel(percent: Int): String = when (percent.coerceIn(0, 100)) {
  0 -> "No carry-over"
  in 1..29 -> "Low carry-over"
  in 30..59 -> "Moderate carry-over"
  else -> "High carry-over"
}
