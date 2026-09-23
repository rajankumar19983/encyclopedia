package com.rajankumar.encyclopaedia.feature.importer

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class OcrDraftValidationTest {
  @Test fun cleanSixOptionDraftPassesValidation() {
    val draft = ParsedQuestionDraft("Which option is correct?", List(6) { "Choice ${it + 1}" }, "F")
    assertTrue(validateOcrDraft(draft).clean)
  }

  @Test fun invalidAnswerAndDuplicateOptionsAreReported() {
    val draft = ParsedQuestionDraft("Question text", listOf("Same", "same"), "C")
    val validation = validateOcrDraft(draft)
    assertFalse(validation.clean)
    assertTrue(validation.warnings.any { it.contains("Duplicate option") })
    assertTrue(validation.warnings.any { it.contains("does not map") })
  }
}
