package com.rajankumar.encyclopaedia.feature.backup

fun restoreReplacementWarning(manifest: BackupManifest): String = if (manifest.totalRecords == 0) {
  "This backup contains no study records. Restoring it may leave your local library empty."
} else {
  "Restore will replace local study data with ${manifest.totalRecords} records from this backup. Create a fresh backup first if you may need to undo this restore."
}
