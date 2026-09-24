package com.rajankumar.encyclopaedia.feature.accessibility

fun speechDurationLabel(text: String): String = when (val minutes = estimatedSpeechMinutes(text)) {
  0 -> "No reading time"
  1 -> "About 1 min"
  else -> "About $minutes min"
}
