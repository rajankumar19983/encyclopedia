package com.rajankumar.encyclopaedia.feature.backup

fun BackupRestoreReviewModel.hasBlockingRestoreIssues(): Boolean = blockingIssueCount > 0
fun BackupRestoreReviewModel.hasRestoreWarnings(): Boolean = warningCount > 0
fun BackupRestoreReviewModel.isCleanForRestore(): Boolean = blockingIssueCount == 0 && warningCount == 0
