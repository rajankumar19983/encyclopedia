package com.rajankumar.encyclopaedia.feature.importer

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class OcrReviewFilterTest {
  @Test fun allMatchesEveryDecision() = OcrReviewDecision.entries.forEach { assertTrue(OcrReviewFilter.ALL.matches(it)) }
  @Test fun pendingMatchesOnlyPending() {
    assertTrue(OcrReviewFilter.PENDING.matches(OcrReviewDecision.PENDING))
    assertFalse(OcrReviewFilter.PENDING.matches(OcrReviewDecision.APPROVED))
  }
}
