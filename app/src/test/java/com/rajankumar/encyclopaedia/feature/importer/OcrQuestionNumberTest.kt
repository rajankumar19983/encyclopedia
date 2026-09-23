package com.rajankumar.encyclopaedia.feature.importer

import org.junit.Assert.assertEquals
import org.junit.Test

class OcrQuestionNumberTest {
  @Test fun extractsCommonNumberedQuestionFormats() {
    val numbers = extractQuestionNumbers("Q1. First?\n2) Second?\nQuestion 3: Third?")
    assertEquals(listOf(1, 2, 3), numbers)
  }
}
