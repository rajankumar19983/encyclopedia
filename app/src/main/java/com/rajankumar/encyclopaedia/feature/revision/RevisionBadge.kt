package com.rajankumar.encyclopaedia.feature.revision

data class RevisionBadge(val count: Int, val hasUrgent: Boolean) {
  val label: String get() = when {
    count <= 0 -> "No questions due"
    hasUrgent -> "$count due, urgent revision available"
    else -> "$count due for revision"
  }
}

fun List<RevisionItem>.revisionBadge() = RevisionBadge(size, any { it.priority == RevisionPriority.URGENT })
