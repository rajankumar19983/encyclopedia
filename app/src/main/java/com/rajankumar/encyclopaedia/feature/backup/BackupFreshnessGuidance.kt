package com.rajankumar.encyclopaedia.feature.backup

fun BackupAge.guidance(): String = when (this) {
  BackupAge.TODAY -> "Your latest backup is current."
  BackupAge.RECENT -> "Consider creating another backup after major study changes."
  BackupAge.STALE -> "Create a new backup before making large changes or restoring data."
}
