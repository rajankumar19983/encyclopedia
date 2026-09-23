package com.rajankumar.encyclopaedia.feature.integrity

import org.junit.Assert.assertTrue
import org.junit.Test

class IntegritySummaryTextTest {
  @Test
  fun healthySummaryIncludesRecordCount() {
    assertTrue(IntegrityReport(emptySet(), 9).summaryText().contains("9 records"))
  }

  @Test
  fun blockedSummaryIncludesBlockingCount() {
    val text = IntegrityReport(setOf(IntegrityIssue.IDS, IntegrityIssue.FIELDS), 2).summaryText()
    assertTrue(text.contains("1 blocking"))
    assertTrue(text.contains("1 warning"))
  }
}
