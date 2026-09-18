package com.rajankumar.encyclopaedia.feature.revision

data class RevisionStats(val total: Int, val urgent: Int, val high: Int, val repeatedMistakes: Int)

fun List<RevisionItem>.revisionStats() = RevisionStats(
  total = size,
  urgent = count { it.priority == RevisionPriority.URGENT },
  high = count { it.priority == RevisionPriority.HIGH },
  repeatedMistakes = count { RevisionReason.REPEATED_MISTAKE in it.reasons }
)
