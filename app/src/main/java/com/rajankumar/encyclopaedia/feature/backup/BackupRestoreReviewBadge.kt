package com.rajankumar.encyclopaedia.feature.backup

data class BackupRestoreReviewBadge(val label: String, val count: Int)

fun BackupRestoreReviewModel.reviewBadges(): List<BackupRestoreReviewBadge> = buildList {
  if (blockingIssueCount > 0) add(BackupRestoreReviewBadge("Blocking", blockingIssueCount))
  if (warningCount > 0) add(BackupRestoreReviewBadge("Warnings", warningCount))
}
