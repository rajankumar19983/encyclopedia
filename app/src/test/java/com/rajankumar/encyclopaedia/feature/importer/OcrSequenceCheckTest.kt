package com.rajankumar.encyclopaedia.feature.importer

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class OcrSequenceCheckTest {
  @Test fun detectsMissingAndDuplicateQuestionNumbers() {
    val check = checkQuestionSequence(listOf(10, 11, 11, 13))
    assertEquals(listOf(12), check.missing)
    assertEquals(listOf(11), check.duplicates)
    assertTrue(check.needsReview)
  }

  @Test fun continuousSequenceIsClean() = assertFalse(checkQuestionSequence(listOf(4, 5, 6)).needsReview)
}
