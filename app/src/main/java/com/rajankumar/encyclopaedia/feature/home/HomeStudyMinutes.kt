package com.rajankumar.encyclopaedia.feature.home

fun homeStudyTimeLabel(minutes: Int): String = when (val safe = minutes.coerceAtLeast(0)) {
  0 -> "No study time today"
  in 1..59 -> "$safe min studied"
  else -> "${safe / 60}h ${safe % 60}m studied"
}
