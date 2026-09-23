package com.rajankumar.encyclopaedia.feature.integrity

import org.junit.Assert.assertEquals
import org.junit.Test

class IntegrityIssueFilterTest {
  @Test
  fun filtersDetailsBySeverity() {
    val report = IntegrityReport(
      setOf(IntegrityIssue.MANIFEST, IntegrityIssue.FIELDS, IntegrityIssue.QUESTION_TOPICS),
      4
    )
    assertEquals(1, report.issueDetails(IntegritySeverity.ERROR).size)
    assertEquals(2, report.issueDetails(IntegritySeverity.WARNING).size)
  }
}
