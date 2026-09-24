package com.rajankumar.encyclopaedia.feature.revision

enum class RevisionSort { PRIORITY, MOST_MISTAKES, MOST_ATTEMPTED }

fun List<RevisionItem>.sortedForRevision(sort: RevisionSort): List<RevisionItem> = when (sort) {
  RevisionSort.PRIORITY -> sortedWith(compareBy<RevisionItem> { it.priority.ordinal }.thenByDescending { it.mistakes }.thenBy { it.question.id })
  RevisionSort.MOST_MISTAKES -> sortedWith(compareByDescending<RevisionItem> { it.mistakes }.thenBy { it.priority.ordinal }.thenBy { it.question.id })
  RevisionSort.MOST_ATTEMPTED -> sortedWith(compareByDescending<RevisionItem> { it.attempts }.thenBy { it.priority.ordinal }.thenBy { it.question.id })
}
