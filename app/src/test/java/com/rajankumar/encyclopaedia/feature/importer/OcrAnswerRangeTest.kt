package com.rajankumar.encyclopaedia.feature.importer

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class OcrAnswerRangeTest {
  @Test fun acceptsSixthOptionForSixOptionQuestion() {
    val check = checkOcrAnswerRange("6", 6)
    assertEquals("F", check.normalizedAnswer)
    assertTrue(check.valid)
  }

  @Test fun rejectsAnswerOutsideOptions() = assertFalse(checkOcrAnswerRange("F", 5).valid)
}
