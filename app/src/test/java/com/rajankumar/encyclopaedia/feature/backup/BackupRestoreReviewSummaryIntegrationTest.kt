package com.rajankumar.encyclopaedia.feature.backup

import com.rajankumar.encyclopaedia.feature.integrity.IntegrityIssue
import com.rajankumar.encyclopaedia.feature.integrity.IntegrityReport
import org.junit.Assert.assertEquals
import org.junit.Test

class BackupRestoreReviewSummaryIntegrationTest {
  @Test fun summaryMatchesSectionsUsedByRestoreReview() {
    val inspection = BackupInspection(
      BackupPreflight(BackupCompatibility.SUPPORTED, 8, true, true),
      IntegrityReport(setOf(IntegrityIssue.IDS, IntegrityIssue.FIELDS, IntegrityIssue.QUESTION_TOPICS), 8)
    )
    val summary = inspection.restoreReviewSummary()
    val sections = inspection.restoreReviewSections()
    assertEquals(sections.sumOf { it.errorCount + it.warningCount }, summary.issueCount)
    assertEquals(sections.sumOf { it.errorCount }, summary.blockingIssueCount)
    assertEquals(sections.sumOf { it.warningCount }, summary.warningCount)
  }
}
