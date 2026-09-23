package com.rajankumar.encyclopaedia.feature.backup

fun BackupDestination.label(): String = when (this) {
  BackupDestination.DEVICE -> "Device storage"
  BackupDestination.GOOGLE_DRIVE -> "Google Drive"
}

fun BackupType.label(): String = when (this) {
  BackupType.AUTOMATIC -> "Automatic"
  BackupType.MANUAL -> "Manual"
}
