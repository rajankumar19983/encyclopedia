package com.rajankumar.encyclopaedia.feature.revision

fun revisionPriority(mistakes: Int, attempts: Int): RevisionPriority {
  if (attempts <= 0) return RevisionPriority.LOW
  val accuracy = ((attempts - mistakes).coerceAtLeast(0) * 100) / attempts
  return when {
    mistakes >= 3 && accuracy < 50 -> RevisionPriority.URGENT
    mistakes >= 2 || accuracy < 50 -> RevisionPriority.HIGH
    mistakes == 1 || accuracy < 75 -> RevisionPriority.NORMAL
    else -> RevisionPriority.LOW
  }
}
