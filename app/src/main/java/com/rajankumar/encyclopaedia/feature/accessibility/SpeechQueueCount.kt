package com.rajankumar.encyclopaedia.feature.accessibility

fun speechQueueCountLabel(count: Int): String = when (val safe = count.coerceAtLeast(0)) {
  0 -> "Nothing queued"
  1 -> "1 section queued"
  else -> "$safe sections queued"
}
