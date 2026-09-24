package com.rajankumar.encyclopaedia.feature.revision

enum class RevisionPace { SHORT, STANDARD, FOCUSED }

fun revisionPace(queueSize: Int): RevisionPace = when (queueSize.coerceAtLeast(0)) {
  in 0..5 -> RevisionPace.SHORT
  in 6..15 -> RevisionPace.STANDARD
  else -> RevisionPace.FOCUSED
}

fun RevisionPace.label(): String = when (this) {
  RevisionPace.SHORT -> "Short session"
  RevisionPace.STANDARD -> "Standard session"
  RevisionPace.FOCUSED -> "Focused backlog session"
}
