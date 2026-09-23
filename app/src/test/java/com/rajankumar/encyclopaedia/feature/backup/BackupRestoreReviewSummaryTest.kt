package com.rajankumar.encyclopaedia.feature.backup

import com.rajankumar.encyclopaedia.feature.integrity.IntegrityIssue
import com.rajankumar.encyclopaedia.feature.integrity.IntegrityReport
import org.junit.Assert.assertEquals
import org.junit.Test

class BackupRestoreReviewSummaryTest {
  @Test fun summaryAggregatesReviewSections() {
    val inspection = BackupInspection(
      BackupPreflight(BackupCompatibility.SUPPORTED, 8, true, true),
      IntegrityReport(setOf(IntegrityIssue.IDS, IntegrityIssue.FIELDS), 8)
    )
    val summary = inspection.restoreReviewSummary()
    assertEquals(2, summary.issueCount)
    assertEquals(1, summary.blockingIssueCount)
    assertEquals(1, summary.warningCount)
  }
}
