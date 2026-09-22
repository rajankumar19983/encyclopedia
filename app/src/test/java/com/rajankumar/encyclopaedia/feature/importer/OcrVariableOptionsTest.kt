package com.rajankumar.encyclopaedia.feature.importer

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Test

class OcrVariableOptionsTest {
  @Test
  fun keepsEightOptionsAndNumericAnswer() {
    val raw = """
      12. Which item is correct?
      A. One
      B. Two
      C. Three
      D. Four
      E. Five
      F. Six
      G. Seven
      H. Eight
      Answer: 8
    """.trimIndent()

    val draft = OcrQuestionParser.parse(raw).single()
    assertEquals(8, draft.options.size)
    assertEquals("H", draft.correctAnswer)
    assertFalse(draft.warnings.any { it.contains("does not point") })
  }
}
