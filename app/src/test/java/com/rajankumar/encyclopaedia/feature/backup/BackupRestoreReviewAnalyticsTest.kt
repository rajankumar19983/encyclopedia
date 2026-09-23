package com.rajankumar.encyclopaedia.feature.backup

import com.rajankumar.encyclopaedia.feature.integrity.IntegrityIssue
import com.rajankumar.encyclopaedia.feature.integrity.IntegrityReport
import org.junit.Assert.assertEquals
import org.junit.Test

class BackupRestoreReviewAnalyticsTest {
  @Test fun snapshotContainsRestoreReviewCounts() {
    val inspection = BackupInspection(
      BackupPreflight(BackupCompatibility.SUPPORTED, 8, true, true),
      IntegrityReport(setOf(IntegrityIssue.IDS, IntegrityIssue.FIELDS), 8)
    )
    val analytics = inspection.restoreReviewAnalytics()
    assertEquals("BLOCKED_BY_INTEGRITY", analytics.readiness)
    assertEquals(2, analytics.issueCount)
    assertEquals(1, analytics.blockingIssueCount)
    assertEquals(1, analytics.warningCount)
  }
}
