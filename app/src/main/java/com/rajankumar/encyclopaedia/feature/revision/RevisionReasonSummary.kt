package com.rajankumar.encyclopaedia.feature.revision

fun Set<RevisionReason>.revisionReasonSummary(): String = when {
  isEmpty() -> "Needs revision"
  size == 1 -> first().label
  else -> sortedBy { it.ordinal }.joinToString(" • ") { it.label }
}
