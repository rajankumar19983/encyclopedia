package com.rajankumar.encyclopaedia.feature.integrity

import org.junit.Assert.assertEquals
import org.junit.Test

class IntegrityOverviewTest {
  @Test
  fun overviewSummarizesReport() {
    val overview = IntegrityReport(
      setOf(IntegrityIssue.IDS, IntegrityIssue.FIELDS),
      20
    ).overview()
    assertEquals(IntegrityStatus.BLOCKED, overview.status)
    assertEquals(20, overview.recordCount)
    assertEquals(1, overview.errorCount)
    assertEquals(1, overview.warningCount)
  }
}
