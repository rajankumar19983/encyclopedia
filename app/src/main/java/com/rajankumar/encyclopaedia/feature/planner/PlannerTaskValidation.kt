package com.rajankumar.encyclopaedia.feature.planner

const val MAX_PLANNER_TASK_LENGTH = 160

fun normalizePlannerTaskTitle(value: String): String = value.trim().replace(Regex("\\s+"), " ").take(MAX_PLANNER_TASK_LENGTH)
fun isValidPlannerTaskTitle(value: String): Boolean = normalizePlannerTaskTitle(value).isNotEmpty()
fun plannerTaskValidationMessage(value: String): String? = when {
  value.isBlank() -> "Enter a study task."
  value.trim().length > MAX_PLANNER_TASK_LENGTH -> "Keep the task within $MAX_PLANNER_TASK_LENGTH characters."
  else -> null
}
