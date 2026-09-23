package com.rajankumar.encyclopaedia.feature.performance

enum class RevisionPriority { HIGH, MEDIUM, LOW }

fun revisionPriority(accuracyPercent: Int, attempts: Int): RevisionPriority = when {
  attempts <= 0 || accuracyPercent < 50 -> RevisionPriority.HIGH
  accuracyPercent < 75 -> RevisionPriority.MEDIUM
  else -> RevisionPriority.LOW
}
