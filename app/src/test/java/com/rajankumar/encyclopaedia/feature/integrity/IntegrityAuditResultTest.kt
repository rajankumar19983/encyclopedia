package com.rajankumar.encyclopaedia.feature.integrity

import org.junit.Assert.*
import org.junit.Test

class IntegrityAuditResultTest {
  @Test
  fun auditResultCombinesDecisionAndPresentation() {
    val result = IntegrityReport(setOf(IntegrityIssue.MANIFEST), 5).auditResult()
    assertFalse(result.decision.allowed)
    assertEquals(IntegrityStatus.BLOCKED, result.report.status)
    assertEquals("Backup cannot be safely restored", result.presentation.headline)
  }
}
