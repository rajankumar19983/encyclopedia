package com.rajankumar.encyclopaedia.feature.planner

private const val MAX_PLANNER_TASK_LENGTH = 160

fun normalizePlannerTaskTitle(value: String): String = value
  .trim()
  .replace(Regex("\\s+"), " ")
  .take(MAX_PLANNER_TASK_LENGTH)

fun isValidPlannerTaskTitle(value: String): Boolean = normalizePlannerTaskTitle(value).isNotEmpty()
