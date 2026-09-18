package com.rajankumar.encyclopaedia.feature.revision

enum class RevisionSort { PRIORITY, MOST_MISTAKES, MOST_ATTEMPTED }

fun List<RevisionItem>.sortedForRevision(sort: RevisionSort): List<RevisionItem> = when (sort) {
  RevisionSort.PRIORITY -> sortedBy { it.priority.ordinal }
  RevisionSort.MOST_MISTAKES -> sortedByDescending { it.mistakes }
  RevisionSort.MOST_ATTEMPTED -> sortedByDescending { it.attempts }
}
