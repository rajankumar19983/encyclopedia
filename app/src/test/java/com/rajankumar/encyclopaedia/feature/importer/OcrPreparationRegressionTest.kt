package com.rajankumar.encyclopaedia.feature.importer

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class OcrPreparationRegressionTest {
  @Test fun mixedSourceRemainsReviewableWithoutHindiQuestionText() {
    val raw = "DSSSB 2025\n1. Which memory is volatile?\nयह प्रश्न हिंदी में है\nA. RAM\nB. ROM\nC. SSD\nD. HDD\nAnswer: A"
    val prepared = prepareOcrImport(raw)
    assertEquals(1, prepared.drafts.size)
    assertFalse(prepared.drafts.single().questionText.contains("हिंदी"))
    assertEquals(4, prepared.drafts.single().options.size)
    assertTrue(prepared.sanitized.removedLines.isNotEmpty())
  }
}
