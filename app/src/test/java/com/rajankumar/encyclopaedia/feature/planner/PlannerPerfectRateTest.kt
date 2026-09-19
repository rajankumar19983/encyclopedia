package com.rajankumar.encyclopaedia.feature.planner

import org.junit.Assert.assertEquals
import org.junit.Test

class PlannerPerfectRateTest {
  @Test
  fun perfectRateUsesRecentPlannedDays() {
    val history = listOf(
      PlannerDayHistory("2026-09-19", 2, 2),
      PlannerDayHistory("2026-09-18", 1, 2),
      PlannerDayHistory("2026-09-17", 3, 3),
      PlannerDayHistory("2026-09-16", 0, 1)
    )
    assertEquals(66, history.recentPerfectDayPercent(3))
  }

  @Test
  fun invalidWindowReturnsZero() {
    assertEquals(0, listOf(PlannerDayHistory("2026-09-19", 1, 1)).recentPerfectDayPercent(0))
  }
}
