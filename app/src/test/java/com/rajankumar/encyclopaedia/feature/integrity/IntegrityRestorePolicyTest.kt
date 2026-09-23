package com.rajankumar.encyclopaedia.feature.integrity

import org.junit.Assert.*
import org.junit.Test

class IntegrityRestorePolicyTest {
  @Test
  fun allowsHealthyAndWarningOnlyReports() {
    assertTrue(IntegrityReport(emptySet(), 0).canRestore())
    assertTrue(IntegrityReport(setOf(IntegrityIssue.FIELDS), 1).canRestore())
  }

  @Test
  fun rejectsReportsWithBlockingIssues() {
    assertFalse(IntegrityReport(setOf(IntegrityIssue.MANIFEST), 1).canRestore())
  }
}
