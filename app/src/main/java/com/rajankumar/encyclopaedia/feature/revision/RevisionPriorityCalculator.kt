package com.rajankumar.encyclopaedia.feature.revision

fun revisionPriority(mistakes: Int, attempts: Int): RevisionPriority {
  if (attempts <= 0) return RevisionPriority.LOW
  val safeMistakes = mistakes.coerceIn(0, attempts)
  val accuracy = ((attempts - safeMistakes) * 100) / attempts
  return when {
    safeMistakes >= 3 && accuracy < 50 -> RevisionPriority.URGENT
    safeMistakes >= 2 || accuracy < 50 -> RevisionPriority.HIGH
    safeMistakes == 1 || accuracy < 75 -> RevisionPriority.NORMAL
    else -> RevisionPriority.LOW
  }
}
