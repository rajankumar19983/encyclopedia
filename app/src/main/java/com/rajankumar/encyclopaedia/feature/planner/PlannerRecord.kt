package com.rajankumar.encyclopaedia.feature.planner

fun List<PlannerDayHistory>.busiestPlannerDay(): PlannerDayHistory? =
  maxWithOrNull(compareBy<PlannerDayHistory> { it.total }.thenBy { it.date })
