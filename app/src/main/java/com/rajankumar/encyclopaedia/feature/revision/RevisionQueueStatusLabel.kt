package com.rajankumar.encyclopaedia.feature.revision

fun RevisionQueueStatus.label(): String = when (this) {
  RevisionQueueStatus.CLEAR -> "Queue clear"
  RevisionQueueStatus.READY -> "Revision ready"
  RevisionQueueStatus.BUSY -> "Revision backlog"
}
