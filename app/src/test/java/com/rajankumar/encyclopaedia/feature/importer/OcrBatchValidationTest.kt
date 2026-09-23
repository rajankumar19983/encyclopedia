package com.rajankumar.encyclopaedia.feature.importer

import org.junit.Assert.assertEquals
import org.junit.Test

class OcrBatchValidationTest {
  @Test fun countsCleanFlaggedAndDuplicateDrafts() {
    val good = ParsedQuestionDraft("Valid question?", listOf("One", "Two"), "A")
    val bad = ParsedQuestionDraft("Bad question?", listOf("Same", "same"), "C")
    val validation = validateOcrBatch(listOf(good, good, bad))
    assertEquals(3, validation.total)
    assertEquals(2, validation.clean)
    assertEquals(1, validation.flagged)
    assertEquals(1, validation.duplicateGroups)
  }
}
