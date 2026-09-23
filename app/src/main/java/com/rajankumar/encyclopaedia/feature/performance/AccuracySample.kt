package com.rajankumar.encyclopaedia.feature.performance

data class AccuracySample(val correct: Int, val attempts: Int) {
  val percent: Int get() = if (attempts <= 0) 0 else ((correct.coerceIn(0, attempts) * 100f) / attempts).toInt().coerceIn(0, 100)
}
