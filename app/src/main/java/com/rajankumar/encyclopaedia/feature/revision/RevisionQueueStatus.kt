package com.rajankumar.encyclopaedia.feature.revision

enum class RevisionQueueStatus { CLEAR, READY, BUSY }

fun revisionQueueStatus(count: Int): RevisionQueueStatus = when (count.coerceAtLeast(0)) {
  0 -> RevisionQueueStatus.CLEAR
  in 1..9 -> RevisionQueueStatus.READY
  else -> RevisionQueueStatus.BUSY
}
