package com.rajankumar.encyclopaedia.feature.performance

enum class AccuracyBand(val label: String) { NEEDS_WORK("Needs work"), DEVELOPING("Developing"), GOOD("Good"), EXCELLENT("Excellent") }

fun accuracyBand(accuracy: Int): AccuracyBand = when (accuracy.coerceIn(0, 100)) {
  in 0..49 -> AccuracyBand.NEEDS_WORK
  in 50..69 -> AccuracyBand.DEVELOPING
  in 70..84 -> AccuracyBand.GOOD
  else -> AccuracyBand.EXCELLENT
}
