package com.rajankumar.encyclopaedia.feature.planner

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class PlannerTaskEditingTest {
  @Test
  fun editValueIsLimitedToPlannerTitleLength() {
    assertEquals(160, plannerTaskEditValue("x".repeat(200)).length)
  }

  @Test
  fun saveRejectsBlankAndAcceptsUsefulTitle() {
    assertFalse(canSavePlannerTaskEdit("   "))
    assertTrue(canSavePlannerTaskEdit("Revise operating systems"))
  }

  @Test
  fun savedTitleUsesPlannerNormalization() {
    assertEquals("Revise DBMS", savedPlannerTaskTitle("  Revise   DBMS  "))
  }
}
