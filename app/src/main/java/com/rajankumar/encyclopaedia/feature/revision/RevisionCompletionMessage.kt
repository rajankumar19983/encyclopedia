package com.rajankumar.encyclopaedia.feature.revision

fun revisionCompletionMessage(correct: Int, total: Int): String {
  if (total <= 0) return "No questions revised"
  val safeCorrect = correct.coerceIn(0, total)
  return "Revision complete: $safeCorrect of $total correct"
}
