package com.rajankumar.encyclopaedia.feature.home

fun HomeDailyGoal.label(): String = when {
  percent >= 100 -> "Daily goal complete"
  safeCompleted == 0 -> "Daily goal not started"
  else -> "$safeCompleted of $safeTarget questions • $percent%"
}
