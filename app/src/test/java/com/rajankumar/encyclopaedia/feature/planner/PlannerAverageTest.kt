package com.rajankumar.encyclopaedia.feature.planner

import org.junit.Assert.assertEquals
import org.junit.Test

class PlannerAverageTest {
  @Test
  fun averageUsesTasksAcrossPlannedDays() {
    val history = listOf(
      PlannerDayHistory("2026-09-19", 2, 3),
      PlannerDayHistory("2026-09-18", 4, 5)
    )

    assertEquals(4.0, history.averageTasksPerPlannedDay(), 0.001)
  }

  @Test
  fun emptyHistoryHasZeroAverage() {
    assertEquals(0.0, emptyList<PlannerDayHistory>().averageTasksPerPlannedDay(), 0.001)
  }
}
