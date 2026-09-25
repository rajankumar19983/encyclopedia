package com.rajankumar.encyclopaedia.feature.questions

fun formatPracticeDuration(milliseconds: Long): String {
  val safeMs = milliseconds.coerceAtLeast(0)
  if (safeMs in 1..999) return "<1s"

  val totalSeconds = safeMs / 1_000
  if (totalSeconds < 60) return "${totalSeconds}s"

  val totalMinutes = totalSeconds / 60
  val seconds = totalSeconds % 60
  if (totalMinutes < 60) return "${totalMinutes}m ${seconds}s"

  val hours = totalMinutes / 60
  val minutes = totalMinutes % 60
  return "${hours}h ${minutes}m"
}
