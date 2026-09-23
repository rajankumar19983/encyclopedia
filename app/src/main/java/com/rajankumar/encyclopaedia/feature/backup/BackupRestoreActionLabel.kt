package com.rajankumar.encyclopaedia.feature.backup

fun BackupRestoreReadiness.actionLabel(): String = when (this) {
  BackupRestoreReadiness.READY -> "Restore backup"
  BackupRestoreReadiness.REVIEW_WARNINGS -> "Review warnings"
  BackupRestoreReadiness.BLOCKED_BY_COMPATIBILITY,
  BackupRestoreReadiness.BLOCKED_BY_INTEGRITY -> "Restore unavailable"
}
