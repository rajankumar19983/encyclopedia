package com.rajankumar.encyclopaedia.feature.backup

fun List<BackupRestoreReviewSection>.orderedForRestoreReview(): List<BackupRestoreReviewSection> =
  sortedWith(compareByDescending<BackupRestoreReviewSection> { it.hasBlockingIssues }
    .thenByDescending { it.errorCount + it.warningCount })
