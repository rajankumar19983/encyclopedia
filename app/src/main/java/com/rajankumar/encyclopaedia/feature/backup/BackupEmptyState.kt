package com.rajankumar.encyclopaedia.feature.backup

data class BackupEmptyState(val title: String, val action: String)

val backupEmptyState = BackupEmptyState(
  title = "Create your first recovery point",
  action = "Create backup"
)
