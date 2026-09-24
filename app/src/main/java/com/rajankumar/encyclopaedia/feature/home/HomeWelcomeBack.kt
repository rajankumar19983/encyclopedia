package com.rajankumar.encyclopaedia.feature.home

fun homeReturnMessage(daysAway: Int): String = when (val safe = daysAway.coerceAtLeast(0)) {
  0 -> "Keep today's momentum going."
  1 -> "Welcome back. Continue where you left off yesterday."
  else -> "Welcome back after $safe days. Start with a manageable study session."
}
