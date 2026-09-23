package com.rajankumar.encyclopaedia.feature.backup

import com.rajankumar.encyclopaedia.feature.integrity.IntegrityIssue
import com.rajankumar.encyclopaedia.feature.integrity.IntegrityReport
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class BackupRestoreReviewSessionTest {
  private fun inspection(issues: Set<IntegrityIssue>) = BackupInspection(
    BackupPreflight(BackupCompatibility.SUPPORTED, 8, true, true),
    IntegrityReport(issues, 8)
  )

  @Test fun cleanBackupCanExecuteImmediately() {
    val session = inspection(emptySet()).restoreReviewSession()
    assertTrue(session.canExecuteRestore)
    assertFalse(session.acknowledgementRequired)
  }

  @Test fun warningBackupWaitsForAcknowledgement() {
    val inspection = inspection(setOf(IntegrityIssue.FIELDS))
    val pending = inspection.restoreReviewSession(false)
    assertFalse(pending.canExecuteRestore)
    assertTrue(pending.acknowledgementRequired)

    val acknowledged = inspection.restoreReviewSession(true)
    assertTrue(acknowledged.canExecuteRestore)
    assertFalse(acknowledged.acknowledgementRequired)
  }

  @Test fun blockingBackupCannotExecuteAfterAcknowledgement() {
    val session = inspection(setOf(IntegrityIssue.IDS)).restoreReviewSession(true)
    assertFalse(session.canExecuteRestore)
    assertFalse(session.acknowledgementRequired)
  }
}
