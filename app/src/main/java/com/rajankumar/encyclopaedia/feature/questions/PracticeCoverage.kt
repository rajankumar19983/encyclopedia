package com.rajankumar.encyclopaedia.feature.questions

data class PracticeCoverage(val practised: Int, val totalQuestions: Int) {
  val percent: Int get() = if (totalQuestions <= 0) 0 else ((practised * 100f) / totalQuestions).toInt().coerceIn(0, 100)
  val fraction: Float get() = percent / 100f
}
