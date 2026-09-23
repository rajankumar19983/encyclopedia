package com.rajankumar.encyclopaedia.feature.performance

fun revisionScore(accuracyPercent: Int, attempts: Int): Int {
  val weakness = 100 - accuracyPercent.coerceIn(0, 100)
  val confidencePenalty = (5 - attempts.coerceIn(0, 5)) * 4
  return (weakness + confidencePenalty).coerceIn(0, 100)
}
