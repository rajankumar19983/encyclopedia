package com.rajankumar.encyclopaedia.feature.revision

data class RevisionSession(val items: List<RevisionItem>, val limit: Int = 20) {
  val questions get() = items.take(limit.coerceIn(1, 50)).map { it.question }
}
