package com.rajankumar.encyclopaedia.feature.importer

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Test

class OcrQuestionParserRequirementTest {
  @Test fun parsesSixOptionQuestionAndAnswer() {
    val raw = """
      1. Which value is correct?
      A. One
      B. Two
      C. Three
      D. Four
      E. Five
      F. Six
      Answer: F
    """.trimIndent()
    val draft = OcrQuestionParser.parse(raw).single()
    assertEquals(6, draft.options.size)
    assertEquals("F", draft.correctAnswer)
    assertFalse(draft.warnings.any { it.contains("answer does not point") })
  }
}
