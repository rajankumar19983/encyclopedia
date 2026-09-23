package com.rajankumar.encyclopaedia.feature.backup

import com.rajankumar.encyclopaedia.feature.integrity.IntegrityIssue
import com.rajankumar.encyclopaedia.feature.integrity.IntegrityReport
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class BackupRestoreReviewFlowTest {
  private fun inspection(issues: Set<IntegrityIssue>) = BackupInspection(
    BackupPreflight(BackupCompatibility.SUPPORTED, 8, true, true),
    IntegrityReport(issues, 8)
  )

  @Test fun cleanBackupCanRestoreWithoutAcknowledgement() {
    val presentation = inspection(emptySet()).restoreReviewSessionPresentation(false)
    assertTrue(presentation.actionEnabled)
    assertFalse(presentation.acknowledgementRequired)
  }

  @Test fun warningsRequireReviewBeforeRestore() {
    val pending = inspection(setOf(IntegrityIssue.FIELDS)).restoreReviewSessionPresentation(false)
    assertFalse(pending.actionEnabled)
    assertTrue(pending.acknowledgementRequired)
    val acknowledged = inspection(setOf(IntegrityIssue.FIELDS)).restoreReviewSessionPresentation(true)
    assertTrue(acknowledged.actionEnabled)
    assertFalse(acknowledged.acknowledgementRequired)
  }

  @Test fun blockingIssuesNeverEnableRestore() {
    assertFalse(inspection(setOf(IntegrityIssue.IDS)).restoreReviewSessionPresentation(true).actionEnabled)
  }
}
