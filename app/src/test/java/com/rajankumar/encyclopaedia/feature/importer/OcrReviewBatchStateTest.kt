package com.rajankumar.encyclopaedia.feature.importer

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class OcrReviewBatchStateTest {
  @Test fun batchSummaryAndCompletionShareDecisionState() {
    val batch = OcrReviewBatchState(listOf(OcrDraftReviewState(decision = OcrReviewDecision.APPROVED), OcrDraftReviewState()))
    assertTrue(batch.summary.detail.contains("1 pending"))
    assertFalse(batch.completion.complete)
  }
}
