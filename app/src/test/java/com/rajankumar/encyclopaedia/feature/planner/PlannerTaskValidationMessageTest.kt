package com.rajankumar.encyclopaedia.feature.planner

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class PlannerTaskValidationMessageTest {
  @Test fun blankTaskGetsFeedback() = assertEquals("Enter a study task.", plannerTaskValidationMessage("   "))
  @Test fun validTaskHasNoError() = assertNull(plannerTaskValidationMessage("Revise DBMS normalization"))
  @Test fun oversizedTaskGetsFeedback() = assertEquals("Keep the task within 160 characters.", plannerTaskValidationMessage("x".repeat(161)))
}
