package com.rajankumar.encyclopaedia.feature.integrity

import org.junit.Assert.assertEquals
import org.junit.Test

class IntegrityCheckWarningPresentationTest {
  @Test fun warningOnlyReportRequestsReviewWithoutCallingDataBlocked() {
    val presentation = IntegrityReport(setOf(IntegrityIssue.FIELDS), 5).toCheckPresentation()
    assertEquals("Data integrity warnings found", presentation.title)
    assertEquals("Review recommended", presentation.status)
    assertEquals(0, presentation.blockingIssues)
    assertEquals(1, presentation.warnings)
  }
}
