package com.rajankumar.encyclopaedia.feature.planner

import org.junit.Assert.assertEquals
import org.junit.Test

class PlannerConsistencyTest {
  @Test
  fun consistencyMeasuresFullyCompletedPlannedDays() {
    val history = listOf(
      PlannerDayHistory("2026-09-19", 3, 3),
      PlannerDayHistory("2026-09-18", 1, 2),
      PlannerDayHistory("2026-09-17", 4, 4),
      PlannerDayHistory("2026-09-16", 0, 2)
    )

    assertEquals(50, history.consistencyPercent())
  }

  @Test
  fun emptyHistoryHasZeroConsistency() {
    assertEquals(0, emptyList<PlannerDayHistory>().consistencyPercent())
  }
}
