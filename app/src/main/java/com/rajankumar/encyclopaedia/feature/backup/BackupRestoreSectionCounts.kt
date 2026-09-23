package com.rajankumar.encyclopaedia.feature.backup

val BackupRestoreReviewModel.blockingSectionCount: Int
  get() = sections.count { it.hasBlockingIssues }

val BackupRestoreReviewModel.warningOnlySectionCount: Int
  get() = sections.count { !it.hasBlockingIssues && it.warningCount > 0 }
