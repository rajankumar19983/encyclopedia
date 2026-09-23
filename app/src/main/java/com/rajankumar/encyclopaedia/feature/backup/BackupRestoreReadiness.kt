package com.rajankumar.encyclopaedia.feature.backup

data class BackupRestoreReadiness(val ready: Boolean, val reason: String?)

fun restoreReadiness(version: Int, integrityValid: Boolean): BackupRestoreReadiness = when {
  backupCompatibility(version) {
    BackupCompatibility.TOO_OLD -> BackupRestoreReadiness(false, "This backup is too old for this app version.")
    BackupCompatibility.TOO_NEW -> BackupRestoreReadiness(false, "Update the app before restoring this newer backup.")
    BackupCompatibility.SUPPORTED -> if (integrityValid) BackupRestoreReadiness(true, null) else BackupRestoreReadiness(false, "Backup integrity check failed.")
  }
}
