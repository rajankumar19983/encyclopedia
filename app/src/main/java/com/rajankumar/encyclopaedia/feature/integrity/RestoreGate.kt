package com.rajankumar.encyclopaedia.feature.integrity

import com.rajankumar.encyclopaedia.feature.backup.BackupSnapshot
import com.rajankumar.encyclopaedia.feature.backup.isSafeToRestore

fun BackupSnapshot.passesRestoreIntegrityGate(): Boolean =
  isSafeToRestore() &&
    hasValidDomainIds() &&
    hasValidDomainFields() &&
    hasValidStudyRelationships() &&
    hasValidNotebookRelationships()

fun BackupSnapshot.requireRestoreIntegrity() {
  require(passesRestoreIntegrityGate()) { "Backup failed integrity validation and cannot be restored." }
}
