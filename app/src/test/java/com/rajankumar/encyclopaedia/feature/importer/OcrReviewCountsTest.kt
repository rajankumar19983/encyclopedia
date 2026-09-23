package com.rajankumar.encyclopaedia.feature.importer

import org.junit.Assert.assertEquals
import org.junit.Test

class OcrReviewCountsTest {
  @Test fun countsEachDecision() {
    val counts = countOcrReviewDecisions(listOf(OcrReviewDecision.PENDING, OcrReviewDecision.APPROVED, OcrReviewDecision.REJECTED, OcrReviewDecision.APPROVED))
    assertEquals(OcrReviewCounts(1, 2, 1), counts)
  }
}
