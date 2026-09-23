package com.rajankumar.encyclopaedia.feature.performance

fun revisionReason(accuracyPercent: Int, attempts: Int): String = when {
  attempts <= 0 -> "Not practised yet"
  accuracyPercent < 50 -> "Frequent mistakes"
  accuracyPercent < 75 -> "Needs reinforcement"
  else -> "Keep fresh"
}
