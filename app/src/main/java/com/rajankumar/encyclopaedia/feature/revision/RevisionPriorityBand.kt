package com.rajankumar.encyclopaedia.feature.revision

fun revisionPriorityFromMistakeRate(percent: Int): RevisionPriority = when (percent.coerceIn(0, 100)) {
  in 75..100 -> RevisionPriority.URGENT
  in 50..74 -> RevisionPriority.HIGH
  in 25..49 -> RevisionPriority.NORMAL
  else -> RevisionPriority.LOW
}
