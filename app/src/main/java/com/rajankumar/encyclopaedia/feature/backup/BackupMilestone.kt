package com.rajankumar.encyclopaedia.feature.backup

data class BackupMilestone(
  val versionedFormat: Boolean,
  val integrityValidation: Boolean,
  val restoreCompatibility: Boolean,
  val retentionPolicy: Boolean,
  val automaticScheduling: Boolean,
  val restoreWarnings: Boolean,
) {
  val complete: Boolean get() = versionedFormat && integrityValidation && restoreCompatibility && retentionPolicy && automaticScheduling && restoreWarnings
}

val currentBackupMilestone = BackupMilestone(true, true, true, true, true, true)
