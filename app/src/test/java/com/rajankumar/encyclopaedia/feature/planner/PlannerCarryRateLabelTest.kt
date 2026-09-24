package com.rajankumar.encyclopaedia.feature.planner

import org.junit.Assert.assertEquals
import org.junit.Test

class PlannerCarryRateLabelTest {
  @Test fun labelsCarryRateBands() {
    assertEquals("No carry-over", carryOverRateLabel(0))
    assertEquals("Low carry-over", carryOverRateLabel(20))
    assertEquals("Moderate carry-over", carryOverRateLabel(40))
    assertEquals("High carry-over", carryOverRateLabel(80))
  }
}
