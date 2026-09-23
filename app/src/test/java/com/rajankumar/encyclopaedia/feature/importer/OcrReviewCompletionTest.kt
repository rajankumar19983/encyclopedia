package com.rajankumar.encyclopaedia.feature.importer

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class OcrReviewCompletionTest {
  @Test fun reportsHumanReadableUnresolvedPositions() {
    val completion = reviewCompletion(listOf(OcrReviewDecision.APPROVED, OcrReviewDecision.PENDING, OcrReviewDecision.REJECTED))
    assertFalse(completion.complete)
    assertEquals(listOf(1), completion.unresolvedIndexes)
  }

  @Test fun allDecidedIsComplete() = assertTrue(reviewCompletion(listOf(OcrReviewDecision.APPROVED, OcrReviewDecision.REJECTED)).complete)
}
