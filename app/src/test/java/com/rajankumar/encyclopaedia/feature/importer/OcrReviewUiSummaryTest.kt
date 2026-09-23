package com.rajankumar.encyclopaedia.feature.importer

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class OcrReviewUiSummaryTest {
  @Test fun pendingSummaryShowsRemaining() {
    val summary = buildOcrReviewUiSummary(listOf(OcrReviewDecision.APPROVED, OcrReviewDecision.PENDING))
    assertEquals("1 remaining", summary.headline)
    assertTrue(summary.detail.contains("1 approved"))
  }
}
