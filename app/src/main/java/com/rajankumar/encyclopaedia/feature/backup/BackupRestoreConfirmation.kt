package com.rajankumar.encyclopaedia.feature.backup

data class BackupRestoreConfirmation(val title: String, val confirmLabel: String)

val backupRestoreConfirmation = BackupRestoreConfirmation(
  title = "Replace local study data?",
  confirmLabel = "Restore backup"
)
