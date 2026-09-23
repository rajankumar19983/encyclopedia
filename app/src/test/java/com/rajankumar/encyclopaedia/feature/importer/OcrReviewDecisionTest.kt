package com.rajankumar.encyclopaedia.feature.importer

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class OcrReviewDecisionTest {
  @Test fun pendingDecisionIsUnresolved() = assertFalse(OcrReviewDecision.PENDING.isResolved())

  @Test fun approvedAndRejectedAreResolved() {
    assertTrue(OcrReviewDecision.APPROVED.isResolved())
    assertTrue(OcrReviewDecision.REJECTED.isResolved())
  }
}
