package com.rajankumar.encyclopaedia.feature.planner

fun plannerTaskEditValue(value: String): String = value.take(MAX_PLANNER_TASK_LENGTH)
fun canSavePlannerTaskEdit(value: String): Boolean = isValidPlannerTaskTitle(value) && value.trim().length <= MAX_PLANNER_TASK_LENGTH
fun savedPlannerTaskTitle(value: String): String = normalizePlannerTaskTitle(value)
