package com.rajankumar.encyclopaedia.feature.planner

import com.rajankumar.encyclopaedia.data.local.PlannerTaskEntity

fun List<PlannerTaskEntity>.carryOverRatePercent(): Int {
  if (isEmpty()) return 0
  return (count { it.carriedFromDate != null } * 100) / size
}
