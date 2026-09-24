package com.rajankumar.encyclopaedia.feature.revision

fun RevisionPriority.displayLabel(): String = when (this) {
  RevisionPriority.URGENT -> "Urgent priority"
  RevisionPriority.HIGH -> "High priority"
  RevisionPriority.NORMAL -> "Normal priority"
  RevisionPriority.LOW -> "Low priority"
}
