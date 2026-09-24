package com.rajankumar.encyclopaedia.feature.home

fun HomeStatModel.accessibilityDescription(): String = "$title: $value. $detail"
fun HomePlanRecommendation.accessibilityDescription(): String = "$title. $detail"
fun HomeTodayPlan.accessibilityDescription(): String = buildString {
  append("Today's plan. $summary.")
  append(" ${(progress * 100).toInt().coerceIn(0, 100)} percent complete.")
  nextTask?.let { append(" Next task: $it.") }
}
