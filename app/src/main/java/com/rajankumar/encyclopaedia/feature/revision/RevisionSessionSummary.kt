package com.rajankumar.encyclopaedia.feature.revision

data class RevisionSessionSummary(
  val completed: Int,
  val total: Int,
  val remaining: Int,
  val isComplete: Boolean
)

fun RevisionSessionState.summary(): RevisionSessionSummary {
  val progress = progress
  return RevisionSessionSummary(
    completed = progress.completed,
    total = progress.total,
    remaining = (progress.total - progress.completed).coerceAtLeast(0),
    isComplete = isComplete
  )
}
