package com.rajankumar.encyclopaedia.feature.importer

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class OcrBatchReviewTest {
  @Test fun countsResolvedAndPendingDrafts() {
    val progress = batchReviewProgress(listOf(OcrReviewDecision.APPROVED, OcrReviewDecision.REJECTED, OcrReviewDecision.PENDING))
    assertEquals(2, progress.resolved)
    assertEquals(1, progress.pending)
    assertFalse(progress.complete)
  }

  @Test fun batchCompletesOnlyWhenEveryDraftIsResolved() {
    assertTrue(batchReviewProgress(listOf(OcrReviewDecision.APPROVED, OcrReviewDecision.REJECTED)).complete)
    assertFalse(batchReviewProgress(emptyList()).complete)
  }
}
