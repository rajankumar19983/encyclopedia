package com.rajankumar.encyclopaedia.feature.backup

enum class BackupRestoreRisk { NONE, CAUTION, BLOCKED }

fun BackupInspection.restoreRisk(): BackupRestoreRisk = when (restoreReadiness()) {
  BackupRestoreReadiness.READY -> BackupRestoreRisk.NONE
  BackupRestoreReadiness.REVIEW_WARNINGS -> BackupRestoreRisk.CAUTION
  BackupRestoreReadiness.BLOCKED_BY_COMPATIBILITY,
  BackupRestoreReadiness.BLOCKED_BY_INTEGRITY -> BackupRestoreRisk.BLOCKED
}
