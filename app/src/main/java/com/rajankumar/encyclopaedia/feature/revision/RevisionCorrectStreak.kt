package com.rajankumar.encyclopaedia.feature.revision

fun revisionCorrectStreakLabel(count: Int): String = when (val safe = count.coerceAtLeast(0)) {
  0 -> "No correct streak"
  1 -> "1 correct in a row"
  else -> "$safe correct in a row"
}
