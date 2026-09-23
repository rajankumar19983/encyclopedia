package com.rajankumar.encyclopaedia.feature.importer

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class OcrBatchDiagnosticsTest {
  @Test fun detectsParserCountMismatch() {
    val diagnostics = buildOcrBatchDiagnostics("1. One?\n2. Two?\n3. Three?", 2)
    assertTrue(diagnostics.needsReview)
  }

  @Test fun matchingContinuousBatchIsClean() {
    val diagnostics = buildOcrBatchDiagnostics("1. One?\n2. Two?", 2)
    assertFalse(diagnostics.needsReview)
  }
}
