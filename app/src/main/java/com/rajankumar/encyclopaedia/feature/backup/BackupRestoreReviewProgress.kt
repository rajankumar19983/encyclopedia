package com.rajankumar.encyclopaedia.feature.backup

data class BackupRestoreReviewProgress(val reviewed: Int, val total: Int) {
  val complete: Boolean get() = reviewed >= total
  val remaining: Int get() = (total - reviewed).coerceAtLeast(0)
}

fun BackupRestoreReviewModel.reviewProgress(reviewedIssueCount: Int): BackupRestoreReviewProgress =
  BackupRestoreReviewProgress(
    reviewed = reviewedIssueCount.coerceIn(0, issueCount),
    total = issueCount
  )
