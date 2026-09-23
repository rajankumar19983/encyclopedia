package com.rajankumar.encyclopaedia.feature.integrity

import org.junit.Assert.assertEquals
import org.junit.Test

class IntegrityCountsTest {
  @Test
  fun countsSummarizeIssueTypes() {
    val counts = IntegrityReport(
      setOf(IntegrityIssue.MANIFEST, IntegrityIssue.FIELDS),
      2
    ).counts()
    assertEquals(2, counts.total)
    assertEquals(1, counts.blocking)
    assertEquals(1, counts.warnings)
  }
}
