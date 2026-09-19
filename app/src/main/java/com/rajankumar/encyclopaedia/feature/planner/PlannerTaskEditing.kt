package com.rajankumar.encyclopaedia.feature.planner

fun plannerTaskEditValue(value: String): String = value.take(160)

fun canSavePlannerTaskEdit(value: String): Boolean = isValidPlannerTaskTitle(value)

fun savedPlannerTaskTitle(value: String): String = normalizePlannerTaskTitle(value)
