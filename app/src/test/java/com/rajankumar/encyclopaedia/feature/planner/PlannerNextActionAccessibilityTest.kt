package com.rajankumar.encyclopaedia.feature.planner

import org.junit.Assert.assertEquals
import org.junit.Test

class PlannerNextActionAccessibilityTest {
  @Test fun nextActionReadsTitleAndReason() {
    assertEquals("Next task: Revise OS. Carried from yesterday", PlannerNextAction("1", "Revise OS", "Carried from yesterday").accessibilityText)
  }
}
