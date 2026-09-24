package com.rajankumar.encyclopaedia.feature.performance

fun coverageLabel(value: Int): String = when (value.coerceIn(0, 100)) {
  in 0..24 -> "Just started"
  in 25..49 -> "Growing"
  in 50..74 -> "Broad"
  else -> "Extensive"
}
