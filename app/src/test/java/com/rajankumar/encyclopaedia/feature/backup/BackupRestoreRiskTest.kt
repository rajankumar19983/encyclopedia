package com.rajankumar.encyclopaedia.feature.backup

import com.rajankumar.encyclopaedia.feature.integrity.IntegrityIssue
import com.rajankumar.encyclopaedia.feature.integrity.IntegrityReport
import org.junit.Assert.assertEquals
import org.junit.Test

class BackupRestoreRiskTest {
  private fun risk(issues: Set<IntegrityIssue>) = BackupInspection(
    BackupPreflight(BackupCompatibility.SUPPORTED, 8, true, true),
    IntegrityReport(issues, 8)
  ).restoreRisk()

  @Test fun cleanBackupHasNoRisk() = assertEquals(BackupRestoreRisk.NONE, risk(emptySet()))
  @Test fun warningBackupNeedsCaution() = assertEquals(BackupRestoreRisk.CAUTION, risk(setOf(IntegrityIssue.FIELDS)))
  @Test fun errorBackupIsBlocked() = assertEquals(BackupRestoreRisk.BLOCKED, risk(setOf(IntegrityIssue.IDS)))
}
