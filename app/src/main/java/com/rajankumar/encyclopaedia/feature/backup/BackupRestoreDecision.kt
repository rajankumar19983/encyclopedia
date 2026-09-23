package com.rajankumar.encyclopaedia.feature.backup

enum class BackupRestoreDecision {
  RESTORE,
  REVIEW,
  CANCEL
}

fun BackupInspection.suggestedRestoreDecision(): BackupRestoreDecision = when (restoreReadiness()) {
  BackupRestoreReadiness.READY -> BackupRestoreDecision.RESTORE
  BackupRestoreReadiness.REVIEW_WARNINGS -> BackupRestoreDecision.REVIEW
  BackupRestoreReadiness.BLOCKED_BY_COMPATIBILITY,
  BackupRestoreReadiness.BLOCKED_BY_INTEGRITY -> BackupRestoreDecision.CANCEL
}
