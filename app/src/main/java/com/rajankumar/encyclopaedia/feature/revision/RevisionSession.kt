package com.rajankumar.encyclopaedia.feature.revision

data class RevisionSession(val items: List<RevisionItem>, val limit: Int = RevisionConstants.defaultSessionSize) {
  val questions get() = if (limit <= 0) emptyList() else items.take(limit.coerceAtMost(50)).map { it.question }
  val size get() = questions.size
  val isEmpty get() = questions.isEmpty()
}
