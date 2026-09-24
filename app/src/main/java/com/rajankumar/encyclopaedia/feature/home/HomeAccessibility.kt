package com.rajankumar.encyclopaedia.feature.home

fun HomeStatModel.accessibilityDescription(): String = "$title: $value. $detail"
fun HomePlanRecommendation.accessibilityDescription(): String = "$title. $detail"
fun HomeTodayPlan.accessibilityDescription(): String = buildString {
  append("Today's plan. $summary.")
  nextTask?.let { append(" Next task: $it.") }
}
