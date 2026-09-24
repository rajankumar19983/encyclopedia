package com.rajankumar.encyclopaedia.feature.revision

enum class RevisionPriority { LOW, MEDIUM, HIGH }

fun revisionPriority(wrongAttempts: Int): RevisionPriority = when (wrongAttempts.coerceAtLeast(0)) {
  0 -> RevisionPriority.LOW
  in 1..2 -> RevisionPriority.MEDIUM
  else -> RevisionPriority.HIGH
}
