package com.rajankumar.encyclopaedia.feature.planner

fun List<PlannerDayHistory>.bestPlannerDay(): PlannerDayHistory? =
  filter { it.total > 0 }
    .maxWithOrNull(compareBy<PlannerDayHistory> { it.percent }.thenBy { it.completed }.thenBy { it.date })
