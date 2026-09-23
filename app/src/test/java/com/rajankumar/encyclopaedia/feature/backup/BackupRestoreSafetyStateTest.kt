package com.rajankumar.encyclopaedia.feature.backup

import com.rajankumar.encyclopaedia.feature.integrity.IntegrityIssue
import com.rajankumar.encyclopaedia.feature.integrity.IntegrityReport
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class BackupRestoreSafetyStateTest {
  @Test fun warningStateChangesAfterAcknowledgement() {
    val inspection = BackupInspection(BackupPreflight(BackupCompatibility.SUPPORTED, 8, true, true), IntegrityReport(setOf(IntegrityIssue.FIELDS), 8))
    assertTrue(inspection.restoreSafetyState(false).acknowledgementRequired)
    assertFalse(inspection.restoreSafetyState(false).canRestore)
    assertTrue(inspection.restoreSafetyState(true).canRestore)
  }
}
