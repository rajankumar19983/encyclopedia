package com.rajankumar.encyclopaedia.feature.backup

fun BackupRestoreReviewSessionPresentation.hasVisibleIssues(): Boolean = badges.any { it.count > 0 }
