package com.rajankumar.encyclopaedia.feature.performance

data class RevisionQueueItem(val id: String, val title: String, val accuracyPercent: Int, val attempts: Int) {
  val priority: RevisionPriority get() = revisionPriority(accuracyPercent, attempts)
  val score: Int get() = revisionScore(accuracyPercent, attempts)
  val reason: String get() = revisionReason(accuracyPercent, attempts)
}
