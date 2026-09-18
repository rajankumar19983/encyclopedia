package com.rajankumar.encyclopaedia.feature.questions

fun accuracyPercent(correct: Int, total: Int): Int =
  if (total <= 0) 0 else ((correct * 100f) / total).toInt().coerceIn(0, 100)

fun formatPracticeDuration(milliseconds: Long): String {
  val seconds = (milliseconds.coerceAtLeast(0) / 1000)
  return if (seconds < 60) "${seconds}s" else "${seconds / 60}m ${seconds % 60}s"
}
