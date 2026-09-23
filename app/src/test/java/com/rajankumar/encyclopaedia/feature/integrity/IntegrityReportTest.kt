package com.rajankumar.encyclopaedia.feature.integrity

import org.junit.Assert.*
import org.junit.Test

class IntegrityReportTest {
  @Test
  fun validReportHasNoMessages() {
    val report = IntegrityReport(emptySet(), 12)
    assertTrue(report.valid)
    assertTrue(report.messages.isEmpty())
    assertTrue(report.details.isEmpty())
    assertEquals(0, report.issueCount)
    assertEquals(12, report.recordCount)
  }

  @Test
  fun invalidReportExposesIssueMessagesAndDetails() {
    val report = IntegrityReport(
      linkedSetOf(IntegrityIssue.IDS, IntegrityIssue.FIELDS),
      3
    )
    assertFalse(report.valid)
    assertEquals(2, report.issueCount)
    assertEquals(2, report.messages.size)
    assertEquals(listOf(IntegrityIssue.IDS, IntegrityIssue.FIELDS), report.details.map { it.issue })
    assertTrue(report.messages.any { "identifier" in it })
    assertTrue(report.messages.any { "fields" in it })
  }
}
