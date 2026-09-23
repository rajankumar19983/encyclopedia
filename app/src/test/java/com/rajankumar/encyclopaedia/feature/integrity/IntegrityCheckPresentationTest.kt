package com.rajankumar.encyclopaedia.feature.integrity

import org.junit.Assert.assertEquals
import org.junit.Test

class IntegrityCheckPresentationTest {
  @Test fun cleanReportIsHealthy() {
    val presentation = IntegrityReport(emptySet(), 42).toCheckPresentation()
    assertEquals("Data integrity check passed", presentation.title)
    assertEquals("Healthy", presentation.status)
    assertEquals(42, presentation.recordsChecked)
    assertEquals(0, presentation.blockingIssues)
    assertEquals(0, presentation.warnings)
  }

  @Test fun blockingIssuesTakePriorityOverWarnings() {
    val presentation = IntegrityReport(setOf(IntegrityIssue.IDS, IntegrityIssue.FIELDS), 10).toCheckPresentation()
    assertEquals("Data integrity issues found", presentation.title)
    assertEquals("Action required", presentation.status)
    assertEquals(1, presentation.blockingIssues)
    assertEquals(1, presentation.warnings)
  }
}
