package com.rajankumar.encyclopaedia.feature.importer

import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class OcrDuplicateWarningTest {
  @Test fun noDuplicatesProduceNoWarning() {
    assertNull(duplicateOcrWarning(listOf(ParsedQuestionDraft("One", listOf("A", "B")))))
  }

  @Test fun warningUsesHumanReviewPositions() {
    val draft = ParsedQuestionDraft("Same question", listOf("One", "Two"))
    val warning = duplicateOcrWarning(listOf(draft, draft)).orEmpty()
    assertTrue(warning.contains("1, 2"))
  }
}
