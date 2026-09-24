package com.rajankumar.encyclopaedia.feature.notebook

fun notebookModifiedLabel(ageMinutes: Long): String = when (val safe = ageMinutes.coerceAtLeast(0)) {
  0L -> "Just now"
  in 1L..59L -> "$safe min ago"
  in 60L..1439L -> "${safe / 60} hr ago"
  else -> "${safe / 1440} d ago"
}
