package com.rajankumar.encyclopaedia.feature.integrity

import org.junit.Assert.*
import org.junit.Test

class IntegrityMessageGroupsTest {
  @Test
  fun groupsMessagesBySeverity() {
    val report = IntegrityReport(
      setOf(IntegrityIssue.IDS, IntegrityIssue.FIELDS),
      2
    )
    assertEquals(listOf(IntegrityIssue.IDS.message()), report.blockingMessages())
    assertEquals(listOf(IntegrityIssue.FIELDS.message()), report.warningMessages())
  }
}
