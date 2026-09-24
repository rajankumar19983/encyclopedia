package com.rajankumar.encyclopaedia.feature.planner

import org.junit.Assert.assertEquals
import org.junit.Test

class PlannerBacklogLabelTest {
  @Test fun labelsBacklogBands() {
    assertEquals("No carried backlog", backlogPressureLabel(0))
    assertEquals("Light carried backlog", backlogPressureLabel(20))
    assertEquals("Moderate carried backlog", backlogPressureLabel(50))
    assertEquals("High carried backlog", backlogPressureLabel(90))
  }
}
