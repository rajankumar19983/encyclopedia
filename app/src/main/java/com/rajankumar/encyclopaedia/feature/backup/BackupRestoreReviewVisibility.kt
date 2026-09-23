package com.rajankumar.encyclopaedia.feature.backup

val BackupRestoreReviewModel.hasIssues: Boolean get() = issueCount > 0
val BackupRestoreReviewModel.showWarningReview: Boolean get() = warningCount > 0
val BackupRestoreReviewModel.showBlockingReview: Boolean get() = blockingIssueCount > 0
