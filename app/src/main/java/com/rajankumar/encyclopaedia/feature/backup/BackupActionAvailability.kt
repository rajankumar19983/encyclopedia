package com.rajankumar.encyclopaedia.feature.backup

data class BackupActionAvailability(val canCreate: Boolean, val canRestore: Boolean)

fun backupActionAvailability(hasBackup: Boolean, busy: Boolean): BackupActionAvailability = BackupActionAvailability(
  canCreate = !busy,
  canRestore = hasBackup && !busy
)
