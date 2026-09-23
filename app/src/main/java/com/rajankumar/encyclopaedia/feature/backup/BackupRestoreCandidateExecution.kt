package com.rajankumar.encyclopaedia.feature.backup

fun BackupRestoreCandidate.canExecute(warningsAcknowledged: Boolean): Boolean =
  session(warningsAcknowledged).canExecuteRestore
