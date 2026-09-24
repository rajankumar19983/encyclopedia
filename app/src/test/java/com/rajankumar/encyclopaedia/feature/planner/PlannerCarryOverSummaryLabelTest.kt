package com.rajankumar.encyclopaedia.feature.planner

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class PlannerCarryOverSummaryLabelTest {
  @Test fun emptySummaryHasNoBacklog() {
    val summary = PlannerCarryOverSummary(0, null)
    assertFalse(summary.hasBacklog)
    assertEquals("No carried-over tasks", summary.summary)
  }
  @Test fun backlogUsesPluralizedSummary() {
    val summary = PlannerCarryOverSummary(2, "2026-09-20")
    assertTrue(summary.hasBacklog)
    assertEquals("2 carried-over tasks", summary.summary)
  }
}
