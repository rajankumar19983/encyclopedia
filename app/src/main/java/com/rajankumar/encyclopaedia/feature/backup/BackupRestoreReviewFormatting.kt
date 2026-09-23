package com.rajankumar.encyclopaedia.feature.backup

fun BackupRestoreIssueGroup.label(): String = when (this) {
  BackupRestoreIssueGroup.BACKUP_STRUCTURE -> "Backup structure"
  BackupRestoreIssueGroup.CONTENT -> "Content"
  BackupRestoreIssueGroup.RELATIONSHIPS -> "Relationships"
}

fun BackupRestoreReviewSection.summaryText(): String = when {
  errorCount > 0 && warningCount > 0 -> "$errorCount blocking issue(s), $warningCount warning(s)"
  errorCount > 0 -> "$errorCount blocking issue(s)"
  warningCount > 0 -> "$warningCount warning(s)"
  else -> "No issues"
}
