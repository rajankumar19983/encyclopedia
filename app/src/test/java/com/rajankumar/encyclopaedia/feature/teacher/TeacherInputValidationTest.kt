package com.rajankumar.encyclopaedia.feature.teacher

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class TeacherInputValidationTest {
  @Test fun trimsValidInput() {
    val result = validateTeacherInput("  Explain RAM  ")
    assertTrue(result.valid)
    assertEquals("Explain RAM", result.normalized)
  }

  @Test fun rejectsBlankAndOversizedInput() {
    assertFalse(validateTeacherInput("   ").valid)
    assertFalse(validateTeacherInput("x".repeat(11), maxLength = 10).valid)
  }
}
