package com.rajankumar.encyclopaedia.feature.backup

import com.rajankumar.encyclopaedia.feature.integrity.IntegrityIssue
import com.rajankumar.encyclopaedia.feature.integrity.IntegrityReport
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class BackupRestoreReviewOutcomeTest {
  private fun inspection(issues: Set<IntegrityIssue>) = BackupInspection(
    BackupPreflight(BackupCompatibility.SUPPORTED, 8, true, true),
    IntegrityReport(issues, 8)
  )

  @Test fun cleanBackupCanRestoreImmediately() {
    val outcome = inspection(emptySet()).restoreReviewOutcome(false)
    assertTrue(outcome.canRestore)
    assertFalse(outcome.requiresAcknowledgement)
    assertEquals("Backup is ready to restore", outcome.headline)
    assertEquals("Restore backup", outcome.actionLabel)
  }

  @Test fun warningBackupRequiresAcknowledgement() {
    val before = inspection(setOf(IntegrityIssue.FIELDS)).restoreReviewOutcome(false)
    assertFalse(before.canRestore)
    assertTrue(before.requiresAcknowledgement)
    assertEquals(1, before.warnings)

    val after = inspection(setOf(IntegrityIssue.FIELDS)).restoreReviewOutcome(true)
    assertTrue(after.canRestore)
    assertFalse(after.requiresAcknowledgement)
  }

  @Test fun blockingBackupCannotRestoreEvenWhenAcknowledged() {
    val outcome = inspection(setOf(IntegrityIssue.IDS)).restoreReviewOutcome(true)
    assertFalse(outcome.canRestore)
    assertFalse(outcome.requiresAcknowledgement)
    assertEquals(1, outcome.blockingIssues)
    assertEquals("Restore unavailable", outcome.actionLabel)
  }
}
