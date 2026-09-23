package com.rajankumar.encyclopaedia.feature.importer

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class OcrBatchSafetyTest {
  @Test fun sequenceProblemMarksBatchUnsafe() {
    val safety = OcrBatchDiagnostics(3, 2, listOf(2), emptyList()).safety()
    assertFalse(safety.safeToReview)
    assertTrue(safety.message.contains("Missing question numbers"))
  }

  @Test fun consistentBatchCanProceedToManualReview() {
    val safety = OcrBatchDiagnostics(3, 3, emptyList(), emptyList()).safety()
    assertTrue(safety.safeToReview)
    assertTrue(safety.message.contains("Review each draft"))
  }
}
