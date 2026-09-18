package com.rajankumar.encyclopaedia.feature.performance

fun formatDurationMs(milliseconds: Long): String {
  val seconds = milliseconds.coerceAtLeast(0) / 1000
  return when {
    seconds < 60 -> "${seconds}s"
    else -> "${seconds / 60}m ${seconds % 60}s"
  }
}

fun signedPercent(value: Int): String = when {
  value > 0 -> "+$value%"
  else -> "$value%"
}
