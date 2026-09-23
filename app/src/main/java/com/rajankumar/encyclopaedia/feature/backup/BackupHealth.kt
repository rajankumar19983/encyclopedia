package com.rajankumar.encyclopaedia.feature.backup

enum class BackupHealth { HEALTHY, NEEDS_BACKUP, NO_BACKUP }

fun backupHealth(lastBackupAt: Long?, now: Long): BackupHealth = when {
  lastBackupAt == null -> BackupHealth.NO_BACKUP
  backupAge(lastBackupAt, now) == BackupAge.STALE -> BackupHealth.NEEDS_BACKUP
  else -> BackupHealth.HEALTHY
}
