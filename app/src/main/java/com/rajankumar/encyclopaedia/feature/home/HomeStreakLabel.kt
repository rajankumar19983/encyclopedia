package com.rajankumar.encyclopaedia.feature.home

fun homeStreakLabel(days: Int): String = when (val safe = days.coerceAtLeast(0)) {
  0 -> "Start your streak today"
  1 -> "1 day streak"
  else -> "$safe day streak"
}
