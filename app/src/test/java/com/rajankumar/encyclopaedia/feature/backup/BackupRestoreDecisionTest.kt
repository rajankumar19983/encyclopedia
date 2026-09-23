package com.rajankumar.encyclopaedia.feature.backup

import com.rajankumar.encyclopaedia.feature.integrity.IntegrityIssue
import com.rajankumar.encyclopaedia.feature.integrity.IntegrityReport
import org.junit.Assert.assertEquals
import org.junit.Test

class BackupRestoreDecisionTest {
  private fun inspection(compatibility: BackupCompatibility, issues: Set<IntegrityIssue>) = BackupInspection(
    BackupPreflight(compatibility, 8, true, true),
    IntegrityReport(issues, 8)
  )

  @Test fun readyBackupSuggestsRestore() = assertEquals(
    BackupRestoreDecision.RESTORE,
    inspection(BackupCompatibility.SUPPORTED, emptySet()).suggestedRestoreDecision()
  )

  @Test fun warningsSuggestReview() = assertEquals(
    BackupRestoreDecision.REVIEW,
    inspection(BackupCompatibility.SUPPORTED, setOf(IntegrityIssue.FIELDS)).suggestedRestoreDecision()
  )

  @Test fun blockingIssuesSuggestCancel() = assertEquals(
    BackupRestoreDecision.CANCEL,
    inspection(BackupCompatibility.SUPPORTED, setOf(IntegrityIssue.IDS)).suggestedRestoreDecision()
  )
}
