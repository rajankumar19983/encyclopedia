package com.rajankumar.encyclopaedia.feature.importer

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class OcrReviewDecisionTransitionTest {
  @Test fun pendingCanBeApprovedOrRejected() {
    assertTrue(canTransitionOcrDecision(OcrReviewDecision.PENDING, OcrReviewDecision.APPROVED))
    assertTrue(canTransitionOcrDecision(OcrReviewDecision.PENDING, OcrReviewDecision.REJECTED))
  }

  @Test fun finalDecisionCannotBeSilentlyChanged() = assertFalse(canTransitionOcrDecision(OcrReviewDecision.APPROVED, OcrReviewDecision.REJECTED))
}
