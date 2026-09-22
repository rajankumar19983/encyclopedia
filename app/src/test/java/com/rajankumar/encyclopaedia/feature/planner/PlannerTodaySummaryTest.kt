package com.rajankumar.encyclopaedia.feature.planner

import com.rajankumar.encyclopaedia.data.local.PlannerTaskEntity
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class PlannerTodaySummaryTest {
  @Test
  fun pendingPlanNamesNextPriority() {
    val summary = listOf(PlannerTaskEntity("a", "Revise DBMS", "2026-09-22")).plannerTodaySummary()
    assertEquals(PlannerDayStatus.NOT_STARTED, summary.status)
    assertTrue(summary.supportingText().contains("Revise DBMS"))
  }

  @Test
  fun completedPlanUsesCompletionMessage() {
    val summary = listOf(PlannerTaskEntity("a", "Done", "2026-09-22", isCompleted = true)).plannerTodaySummary()
    assertEquals("Everything planned for today is complete.", summary.supportingText())
  }
}
