package com.rajankumar.encyclopaedia.feature.backup

fun BackupRestoreIssueGroup.label(): String = when (this) {
  BackupRestoreIssueGroup.BACKUP_STRUCTURE -> "Backup structure"
  BackupRestoreIssueGroup.CONTENT -> "Study content"
  BackupRestoreIssueGroup.RELATIONSHIPS -> "Data relationships"
}
