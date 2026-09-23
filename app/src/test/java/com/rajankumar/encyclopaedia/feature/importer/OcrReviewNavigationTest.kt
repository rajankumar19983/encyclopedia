package com.rajankumar.encyclopaedia.feature.importer

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class OcrReviewNavigationTest {
  @Test fun findsNextPendingAndWraps() {
    val decisions = listOf(OcrReviewDecision.PENDING, OcrReviewDecision.APPROVED, OcrReviewDecision.PENDING)
    assertEquals(2, nextPendingReviewIndex(decisions, 0))
    assertEquals(0, nextPendingReviewIndex(decisions, 2))
  }

  @Test fun returnsNullWhenReviewComplete() = assertNull(nextPendingReviewIndex(listOf(OcrReviewDecision.APPROVED), 0))
}
