package com.rajankumar.encyclopaedia.feature.planner

import org.junit.Assert.assertEquals
import org.junit.Test

class PlannerRecentPendingTest {
  @Test
  fun pendingUsesRequestedRecentDays() {
    val history = listOf(
      PlannerDayHistory("2026-09-19", 3, 5),
      PlannerDayHistory("2026-09-18", 1, 4),
      PlannerDayHistory("2026-09-17", 0, 20)
    )
    assertEquals(5, history.recentPendingTasks(2))
  }

  @Test
  fun invalidWindowReturnsZero() {
    assertEquals(0, listOf(PlannerDayHistory("2026-09-19", 0, 5)).recentPendingTasks(0))
  }
}
