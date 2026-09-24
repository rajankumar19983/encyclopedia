package com.rajankumar.encyclopaedia.feature.home

import org.junit.Assert.assertEquals
import org.junit.Test

class HomeQuestionCountTest {
  @Test fun labelsQuestionCounts() { assertEquals("Question bank empty", homeQuestionCountLabel(0)); assertEquals("1 question available", homeQuestionCountLabel(1)); assertEquals("40 questions available", homeQuestionCountLabel(40)) }
}
