package com.rajankumar.encyclopaedia.feature.integrity

import com.rajankumar.encyclopaedia.feature.backup.BackupSnapshot

fun BackupSnapshot.passesRestoreIntegrityGate(): Boolean = integrityIssues().isEmpty()

fun BackupSnapshot.requireRestoreIntegrity() {
  val issues = integrityIssues()
  require(issues.isEmpty()) { "Backup failed integrity validation: ${issues.joinToString()}" }
}
