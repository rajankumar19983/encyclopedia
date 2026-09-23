package com.rajankumar.encyclopaedia.feature.backup

data class BackupPreflightPresentation(
  val title: String,
  val description: String,
  val recordCount: Int,
  val restoreEnabled: Boolean
)

fun BackupPreflight.presentation(): BackupPreflightPresentation = BackupPreflightPresentation(
  title = when (compatibility) {
    BackupCompatibility.SUPPORTED -> "Backup ready"
    BackupCompatibility.TOO_OLD -> "Backup too old"
    BackupCompatibility.TOO_NEW -> "Backup from newer version"
  },
  description = compatibility.description(),
  recordCount = recordCount,
  restoreEnabled = canRestore
)
