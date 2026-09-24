package com.rajankumar.encyclopaedia.feature.revision

fun revisionAnswerFeedback(isCorrect: Boolean, remaining: Int): String = when {
  isCorrect && remaining <= 0 -> "Correct. Revision session complete."
  isCorrect -> "Correct. ${remaining.coerceAtLeast(0)} remaining."
  remaining <= 0 -> "Incorrect. Review this question again soon."
  else -> "Incorrect. Keep it in the revision queue."
}
