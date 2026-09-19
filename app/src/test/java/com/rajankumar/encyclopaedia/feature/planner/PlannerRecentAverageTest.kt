package com.rajankumar.encyclopaedia.feature.planner

import org.junit.Assert.assertEquals
import org.junit.Test

class PlannerRecentAverageTest {
  @Test
  fun averageUsesOnlyRequestedRecentDays() {
    val history = listOf(
      PlannerDayHistory("2026-09-19", 2, 4),
      PlannerDayHistory("2026-09-18", 3, 6),
      PlannerDayHistory("2026-09-17", 1, 20)
    )
    assertEquals(5.0, history.recentAveragePlanSize(2), 0.001)
  }

  @Test
  fun emptyOrInvalidWindowReturnsZero() {
    assertEquals(0.0, emptyList<PlannerDayHistory>().recentAveragePlanSize(), 0.001)
    assertEquals(0.0, listOf(PlannerDayHistory("2026-09-19", 1, 1)).recentAveragePlanSize(0), 0.001)
  }
}
