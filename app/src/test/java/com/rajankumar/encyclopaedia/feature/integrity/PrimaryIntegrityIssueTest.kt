package com.rajankumar.encyclopaedia.feature.integrity

import org.junit.Assert.*
import org.junit.Test

class PrimaryIntegrityIssueTest {
  @Test
  fun prioritizesBlockingIssue() {
    val report = IntegrityReport(
      linkedSetOf(IntegrityIssue.FIELDS, IntegrityIssue.IDS),
      2
    )
    assertEquals(IntegrityIssue.IDS, report.primaryIssue()?.issue)
  }

  @Test
  fun returnsNullForHealthyReport() = assertNull(
    IntegrityReport(emptySet(), 0).primaryIssue()
  )
}
