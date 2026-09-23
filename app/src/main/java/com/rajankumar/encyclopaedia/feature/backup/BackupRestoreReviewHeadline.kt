package com.rajankumar.encyclopaedia.feature.backup

fun BackupRestoreReviewModel.reviewHeadline(): String = when (status()) {
  BackupRestoreReviewStatus.CLEAN -> "Backup is ready to restore"
  BackupRestoreReviewStatus.WARNINGS -> "$warningCount warning${if (warningCount == 1) "" else "s"} to review"
  BackupRestoreReviewStatus.BLOCKED -> "$blockingIssueCount blocking issue${if (blockingIssueCount == 1) "" else "s"} found"
}
