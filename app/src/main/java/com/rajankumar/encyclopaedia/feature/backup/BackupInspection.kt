package com.rajankumar.encyclopaedia.feature.backup

import com.rajankumar.encyclopaedia.feature.integrity.IntegrityReport
import com.rajankumar.encyclopaedia.feature.integrity.integrityReport

data class BackupInspection(
  val preflight: BackupPreflight,
  val integrity: IntegrityReport?
) {
  val canRestore: Boolean
    get() = preflight.canRestore && integrity?.hasBlockingIssues != true
}

fun BackupSnapshot.inspectForRestore(): BackupInspection {
  val preflight = preflight()
  return BackupInspection(
    preflight = preflight,
    integrity = if (preflight.canInspect && preflight.compatibility == BackupCompatibility.SUPPORTED) {
      integrityReport()
    } else {
      null
    }
  )
}
