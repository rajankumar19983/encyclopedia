package com.rajankumar.encyclopaedia.feature.backup

enum class BackupRestoreReviewStatus { CLEAN, WARNINGS, BLOCKED }

fun BackupRestoreReviewModel.status(): BackupRestoreReviewStatus = when {
  blockingIssueCount > 0 -> BackupRestoreReviewStatus.BLOCKED
  warningCount > 0 -> BackupRestoreReviewStatus.WARNINGS
  else -> BackupRestoreReviewStatus.CLEAN
}
