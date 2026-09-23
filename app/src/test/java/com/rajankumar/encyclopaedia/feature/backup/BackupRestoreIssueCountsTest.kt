package com.rajankumar.encyclopaedia.feature.backup

import com.rajankumar.encyclopaedia.feature.integrity.IntegrityIssue
import com.rajankumar.encyclopaedia.feature.integrity.IntegrityReport
import org.junit.Assert.assertEquals
import org.junit.Test

class BackupRestoreIssueCountsTest {
  @Test fun countsErrorsWarningsAndTotal() {
    val inspection = BackupInspection(
      BackupPreflight(BackupCompatibility.SUPPORTED, 8, true, true),
      IntegrityReport(setOf(IntegrityIssue.IDS, IntegrityIssue.FIELDS, IntegrityIssue.QUESTION_TOPICS), 8)
    )
    val counts = inspection.restoreIssueCounts()
    assertEquals(1, counts.errors)
    assertEquals(2, counts.warnings)
    assertEquals(3, counts.total)
  }
}
