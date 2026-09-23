package com.rajankumar.encyclopaedia.feature.backup

enum class BackupCompatibility { SUPPORTED, TOO_OLD, TOO_NEW }

fun backupCompatibility(version: Int): BackupCompatibility = when {
  version < MIN_SUPPORTED_BACKUP_FORMAT_VERSION -> BackupCompatibility.TOO_OLD
  version > BACKUP_FORMAT_VERSION -> BackupCompatibility.TOO_NEW
  else -> BackupCompatibility.SUPPORTED
}
