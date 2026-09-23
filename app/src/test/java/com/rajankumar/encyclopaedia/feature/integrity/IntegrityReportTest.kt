package com.rajankumar.encyclopaedia.feature.integrity

import org.junit.Assert.*
import org.junit.Test

class IntegrityReportTest {
  @Test
  fun validReportHasNoIssues() {
    val report = IntegrityReport(emptySet(), 12)
    assertTrue(report.valid)
    assertFalse(report.hasBlockingIssues)
    assertEquals(0, report.issueCount)
    assertEquals(0, report.errorCount)
    assertEquals(0, report.warningCount)
    assertEquals(12, report.recordCount)
  }

  @Test
  fun reportSeparatesErrorsFromWarnings() {
    val report = IntegrityReport(
      linkedSetOf(IntegrityIssue.IDS, IntegrityIssue.FIELDS, IntegrityIssue.QUESTION_TOPICS),
      3
    )
    assertFalse(report.valid)
    assertTrue(report.hasBlockingIssues)
    assertEquals(3, report.issueCount)
    assertEquals(1, report.errorCount)
    assertEquals(2, report.warningCount)
    assertEquals(IntegritySeverity.ERROR, report.details.first().severity)
  }
}
