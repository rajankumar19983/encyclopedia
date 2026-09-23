package com.rajankumar.encyclopaedia.feature.backup

enum class BackupRestoreReadiness {
  READY,
  REVIEW_WARNINGS,
  BLOCKED_BY_COMPATIBILITY,
  BLOCKED_BY_INTEGRITY
}

fun BackupInspection.restoreReadiness(): BackupRestoreReadiness = when {
  !preflight.canRestore -> BackupRestoreReadiness.BLOCKED_BY_COMPATIBILITY
  integrity?.hasBlockingIssues == true -> BackupRestoreReadiness.BLOCKED_BY_INTEGRITY
  integrity?.warningCount?.let { it > 0 } == true -> BackupRestoreReadiness.REVIEW_WARNINGS
  else -> BackupRestoreReadiness.READY
}
