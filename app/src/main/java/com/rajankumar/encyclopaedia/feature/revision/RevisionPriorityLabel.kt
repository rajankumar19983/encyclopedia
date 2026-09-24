package com.rajankumar.encyclopaedia.feature.revision

fun RevisionPriority.label(): String = when (this) {
  RevisionPriority.LOW -> "Low priority"
  RevisionPriority.MEDIUM -> "Needs revision"
  RevisionPriority.HIGH -> "High priority"
}
