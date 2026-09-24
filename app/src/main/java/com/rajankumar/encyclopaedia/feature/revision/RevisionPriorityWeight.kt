package com.rajankumar.encyclopaedia.feature.revision

fun RevisionPriority.weight(): Int = when (this) {
  RevisionPriority.URGENT -> 4
  RevisionPriority.HIGH -> 3
  RevisionPriority.NORMAL -> 2
  RevisionPriority.LOW -> 1
}
