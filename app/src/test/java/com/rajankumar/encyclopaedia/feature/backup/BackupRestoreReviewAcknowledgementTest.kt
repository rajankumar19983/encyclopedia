package com.rajankumar.encyclopaedia.feature.backup

import com.rajankumar.encyclopaedia.feature.integrity.IntegrityIssue
import com.rajankumar.encyclopaedia.feature.integrity.IntegrityReport
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class BackupRestoreReviewAcknowledgementTest {
  @Test fun warningRequiresAcknowledgement() {
    val inspection = BackupInspection(
      BackupPreflight(BackupCompatibility.SUPPORTED, 8, true, true),
      IntegrityReport(setOf(IntegrityIssue.FIELDS), 8)
    )
    assertFalse(inspection.restoreAcknowledgement(false).canConfirm)
    assertTrue(inspection.restoreAcknowledgement(true).canConfirm)
  }
}
