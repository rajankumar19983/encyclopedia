package com.rajankumar.encyclopaedia.feature.integrity

import com.rajankumar.encyclopaedia.feature.backup.BackupSnapshot

fun BackupSnapshot.passesRestoreIntegrityGate(): Boolean = integrityReport().canRestore()

fun BackupSnapshot.requireRestoreIntegrity() {
  val report = integrityReport()
  require(report.canRestore()) {
    "Backup failed integrity validation: ${report.issueDetails(IntegritySeverity.ERROR).joinToString { it.issue.code() }}"
  }
}
