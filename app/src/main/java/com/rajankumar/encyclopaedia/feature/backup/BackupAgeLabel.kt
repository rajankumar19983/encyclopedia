package com.rajankumar.encyclopaedia.feature.backup

fun BackupAge.label(): String = when (this) {
  BackupAge.TODAY -> "Backed up today"
  BackupAge.RECENT -> "Recent backup"
  BackupAge.STALE -> "Backup is outdated"
}
