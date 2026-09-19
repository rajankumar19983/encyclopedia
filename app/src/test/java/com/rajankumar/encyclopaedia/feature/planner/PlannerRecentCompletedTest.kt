package com.rajankumar.encyclopaedia.feature.planner

import org.junit.Assert.assertEquals
import org.junit.Test

class PlannerRecentCompletedTest {
  @Test
  fun completedUsesRequestedRecentDays() {
    val history = listOf(
      PlannerDayHistory("2026-09-19", 4, 5),
      PlannerDayHistory("2026-09-18", 3, 4),
      PlannerDayHistory("2026-09-17", 10, 10)
    )
    assertEquals(7, history.recentCompletedTasks(2))
  }

  @Test
  fun invalidWindowReturnsZero() {
    assertEquals(0, listOf(PlannerDayHistory("2026-09-19", 4, 5)).recentCompletedTasks(0))
  }
}
