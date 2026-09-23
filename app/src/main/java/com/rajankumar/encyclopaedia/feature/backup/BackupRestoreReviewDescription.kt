package com.rajankumar.encyclopaedia.feature.backup

fun BackupRestoreReviewModel.reviewDescription(): String = when (status()) {
  BackupRestoreReviewStatus.CLEAN -> "No integrity issues were detected in this backup."
  BackupRestoreReviewStatus.WARNINGS -> "Review the warnings before replacing local study data."
  BackupRestoreReviewStatus.BLOCKED -> "Resolve or choose another backup because restore is blocked."
}
