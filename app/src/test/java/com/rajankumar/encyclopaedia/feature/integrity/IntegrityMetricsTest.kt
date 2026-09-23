package com.rajankumar.encyclopaedia.feature.integrity

import org.junit.Assert.assertEquals
import org.junit.Test

class IntegrityMetricsTest {
  @Test
  fun metricsReflectReportCounts() {
    val metrics = IntegrityReport(
      setOf(IntegrityIssue.IDS, IntegrityIssue.FIELDS, IntegrityIssue.QUESTION_TOPICS),
      18
    ).metrics()
    assertEquals(18, metrics.records)
    assertEquals(3, metrics.issues)
    assertEquals(1, metrics.errors)
    assertEquals(2, metrics.warnings)
  }
}
