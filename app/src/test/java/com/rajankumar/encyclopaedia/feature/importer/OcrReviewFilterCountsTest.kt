package com.rajankumar.encyclopaedia.feature.importer

import org.junit.Assert.assertEquals
import org.junit.Test

class OcrReviewFilterCountsTest {
  @Test fun filtersCountExpectedDecisions() {
    val decisions = listOf(OcrReviewDecision.PENDING, OcrReviewDecision.APPROVED, OcrReviewDecision.APPROVED)
    assertEquals(3, OcrReviewFilter.ALL.count(decisions))
    assertEquals(1, OcrReviewFilter.PENDING.count(decisions))
    assertEquals(2, OcrReviewFilter.APPROVED.count(decisions))
  }
}
