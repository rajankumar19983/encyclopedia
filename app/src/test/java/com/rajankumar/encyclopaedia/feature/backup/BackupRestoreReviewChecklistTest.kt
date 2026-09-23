package com.rajankumar.encyclopaedia.feature.backup

import com.rajankumar.encyclopaedia.feature.integrity.IntegrityIssue
import com.rajankumar.encyclopaedia.feature.integrity.IntegrityReport
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class BackupRestoreReviewChecklistTest {
  @Test fun checklistFlattensIntegrityMessages() {
    val inspection = BackupInspection(
      BackupPreflight(BackupCompatibility.SUPPORTED, 8, true, true),
      IntegrityReport(setOf(IntegrityIssue.IDS, IntegrityIssue.FIELDS), 8)
    )
    val checklist = inspection.restoreReviewChecklist()
    assertEquals(2, checklist.size)
    assertTrue(checklist.first().blocking)
  }
}
