package com.rajankumar.encyclopaedia.feature.importer

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class OcrReviewGateTest {
  @Test fun blocksEmptyExtraction() {
    val prepared = prepareOcrImport("DSSSB 2024")
    assertFalse(prepared.reviewGate().canReview)
  }

  @Test fun permitsExtractedDraftWhileSurfacingDiagnostics() {
    val prepared = prepareOcrImport("1. What is RAM?\nA. Memory\nB. Storage\nAnswer: A")
    assertTrue(prepared.reviewGate().canReview)
  }
}
