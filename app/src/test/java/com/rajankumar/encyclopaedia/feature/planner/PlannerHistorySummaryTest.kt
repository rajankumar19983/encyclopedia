package com.rajankumar.encyclopaedia.feature.planner

import org.junit.Assert.assertEquals
import org.junit.Test

class PlannerHistorySummaryTest {
  @Test
  fun summaryAggregatesPlannerHistory() {
    val history = listOf(
      PlannerDayHistory("2026-09-19", 3, 3),
      PlannerDayHistory("2026-09-18", 1, 2),
      PlannerDayHistory("2026-09-17", 4, 4)
    )

    val summary = history.historySummary()

    assertEquals(3, summary.plannedDays)
    assertEquals(2, summary.fullyCompletedDays)
    assertEquals(9, summary.totalTasks)
    assertEquals(8, summary.completedTasks)
    assertEquals(88, summary.overallPercent)
  }

  @Test
  fun emptyHistoryHasZeroSummary() {
    val summary = emptyList<PlannerDayHistory>().historySummary()

    assertEquals(0, summary.plannedDays)
    assertEquals(0, summary.fullyCompletedDays)
    assertEquals(0, summary.totalTasks)
    assertEquals(0, summary.completedTasks)
    assertEquals(0, summary.overallPercent)
  }
}
