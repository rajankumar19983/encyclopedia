package com.rajankumar.encyclopaedia.feature.questions

data class QuestionMastery(val attempts: Int, val correct: Int) {
  val accuracy: Int get() = accuracyPercent(correct, attempts)
  val label: String get() = when {
    attempts == 0 -> "New"
    attempts < 2 -> "Learning"
    accuracy >= 80 -> "Strong"
    accuracy >= 50 -> "Developing"
    else -> "Needs review"
  }
}
