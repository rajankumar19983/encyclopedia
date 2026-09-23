package com.rajankumar.encyclopaedia.feature.backup

data class BackupPreflight(
  val compatibility: BackupCompatibility,
  val recordCount: Int,
  val canInspect: Boolean,
  val canRestore: Boolean
)

fun BackupSnapshot.preflight(): BackupPreflight {
  val compatibility = backupCompatibility(manifest.formatVersion)
  return BackupPreflight(
    compatibility = compatibility,
    recordCount = manifest.totalRecords,
    canInspect = true,
    canRestore = compatibility == BackupCompatibility.SUPPORTED
  )
}
