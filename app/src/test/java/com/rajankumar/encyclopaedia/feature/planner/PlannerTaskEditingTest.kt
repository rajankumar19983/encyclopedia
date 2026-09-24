package com.rajankumar.encyclopaedia.feature.planner

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class PlannerTaskEditingTest {
  @Test fun editValueIsLimitedToPlannerTitleLength() = assertEquals(MAX_PLANNER_TASK_LENGTH, plannerTaskEditValue("x".repeat(200)).length)
  @Test fun saveRejectsBlankAndAcceptsUsefulTitle() {
    assertFalse(canSavePlannerTaskEdit("   "))
    assertTrue(canSavePlannerTaskEdit("Revise operating systems"))
  }
  @Test fun oversizedRawEditIsRejected() = assertFalse(canSavePlannerTaskEdit("x".repeat(MAX_PLANNER_TASK_LENGTH + 1)))
  @Test fun savedTitleUsesPlannerNormalization() = assertEquals("Revise DBMS", savedPlannerTaskTitle("  Revise   DBMS  "))
}
