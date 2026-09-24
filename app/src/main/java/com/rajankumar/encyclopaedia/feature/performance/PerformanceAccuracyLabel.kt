package com.rajankumar.encyclopaedia.feature.performance

fun accuracyLabel(value: Int): String = when (value.coerceIn(0, 100)) {
  in 0..39 -> "Needs attention"
  in 40..59 -> "Developing"
  in 60..79 -> "Good"
  else -> "Strong"
}
