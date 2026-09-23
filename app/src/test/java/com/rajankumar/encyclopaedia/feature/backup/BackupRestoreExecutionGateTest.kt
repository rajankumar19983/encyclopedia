package com.rajankumar.encyclopaedia.feature.backup

import com.rajankumar.encyclopaedia.feature.integrity.IntegrityIssue
import com.rajankumar.encyclopaedia.feature.integrity.IntegrityReport
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class BackupRestoreExecutionGateTest {
  private fun inspection(issues: Set<IntegrityIssue>) = BackupInspection(
    BackupPreflight(BackupCompatibility.SUPPORTED, 8, true, true),
    IntegrityReport(issues, 8)
  )

  @Test fun warningsExecuteOnlyAfterAcknowledgement() {
    val warning = inspection(setOf(IntegrityIssue.FIELDS))
    assertFalse(warning.restoreExecutionGate(false).canExecute)
    assertTrue(warning.restoreExecutionGate(true).canExecute)
  }

  @Test fun blockingIssueNeverExecutes() {
    assertFalse(inspection(setOf(IntegrityIssue.IDS)).restoreExecutionGate(true).canExecute)
  }
}
