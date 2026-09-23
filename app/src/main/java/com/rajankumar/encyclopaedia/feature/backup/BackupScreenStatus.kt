package com.rajankumar.encyclopaedia.feature.backup

data class BackupScreenStatus(val headline: String, val detail: String)

fun backupScreenStatus(health: BackupHealth): BackupScreenStatus = BackupScreenStatus(
  headline = when (health) {
    BackupHealth.HEALTHY -> "Recovery point available"
    BackupHealth.NEEDS_BACKUP -> "Backup recommended"
    BackupHealth.NO_BACKUP -> "No recovery point yet"
  },
  detail = health.message()
)
