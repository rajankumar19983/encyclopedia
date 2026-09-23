package com.rajankumar.encyclopaedia.feature.backup

enum class BackupRestoreSectionPriority { BLOCKING, WARNING }

val BackupRestoreReviewSection.priority: BackupRestoreSectionPriority
  get() = if (hasBlockingIssues) BackupRestoreSectionPriority.BLOCKING else BackupRestoreSectionPriority.WARNING
