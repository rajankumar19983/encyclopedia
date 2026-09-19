package com.rajankumar.encyclopaedia.feature.planner

import org.junit.Assert.assertEquals
import org.junit.Test

class PlannerTrendLabelTest {
  @Test
  fun trendsHaveReadableLabels() {
    assertEquals("Improving", PlannerTrend.IMPROVING.displayText())
    assertEquals("Needs attention", PlannerTrend.DECLINING.displayText())
    assertEquals("Steady", PlannerTrend.STEADY.displayText())
    assertEquals("Not enough history yet", PlannerTrend.INSUFFICIENT_DATA.displayText())
  }
}
