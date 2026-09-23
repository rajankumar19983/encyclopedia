package com.rajankumar.encyclopaedia.feature.backup

data class BackupScreenCopy(val title: String, val subtitle: String)

val defaultBackupScreenCopy = BackupScreenCopy(
  title = "Backup & Restore",
  subtitle = "Protect your local study data and restore it deliberately when needed."
)
