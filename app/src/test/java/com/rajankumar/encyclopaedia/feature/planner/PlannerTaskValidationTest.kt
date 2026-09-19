package com.rajankumar.encyclopaedia.feature.planner

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class PlannerTaskValidationTest {
  @Test
  fun taskTitleIsTrimmedAndWhitespaceCollapsed() {
    assertEquals("Revise operating systems", normalizePlannerTaskTitle("  Revise   operating systems  "))
  }

  @Test
  fun blankTaskTitleIsInvalid() {
    assertFalse(isValidPlannerTaskTitle("   "))
  }

  @Test
  fun normalTaskTitleIsValid() {
    assertTrue(isValidPlannerTaskTitle("Practice DBMS MCQs"))
  }

  @Test
  fun longTaskTitleIsLimited() {
    assertEquals(160, normalizePlannerTaskTitle("a".repeat(200)).length)
  }
}
