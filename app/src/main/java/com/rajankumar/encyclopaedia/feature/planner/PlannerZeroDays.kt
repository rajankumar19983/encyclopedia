package com.rajankumar.encyclopaedia.feature.planner

fun List<PlannerDayHistory>.zeroCompletionDays(): Int = count { it.total > 0 && it.completed == 0 }
