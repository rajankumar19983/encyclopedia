package com.rajankumar.encyclopaedia.feature.performance

fun averageSpeedLabel(averageTimeMs: Long): String = when (averageTimeMs.coerceAtLeast(0)) {
  0L -> "No timing data"
  in 1L..15_000L -> "Fast"
  in 15_001L..45_000L -> "Steady"
  else -> "Slow"
}
