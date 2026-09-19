package com.rajankumar.encyclopaedia.feature.planner

import org.junit.Assert.assertEquals
import org.junit.Test

class PlannerStreakTest {
  @Test
  fun streakCountsConsecutiveFullyCompletedDays() {
    val history = listOf(
      PlannerDayHistory("2026-09-19", 3, 3),
      PlannerDayHistory("2026-09-18", 2, 2),
      PlannerDayHistory("2026-09-17", 4, 4),
      PlannerDayHistory("2026-09-16", 1, 2)
    )

    assertEquals(3, history.completionStreak("2026-09-19"))
  }

  @Test
  fun streakStopsWhenTodayIsIncomplete() {
    val history = listOf(
      PlannerDayHistory("2026-09-19", 1, 2),
      PlannerDayHistory("2026-09-18", 2, 2)
    )

    assertEquals(0, history.completionStreak("2026-09-19"))
  }

  @Test
  fun missingDateBreaksStreak() {
    val history = listOf(
      PlannerDayHistory("2026-09-19", 1, 1),
      PlannerDayHistory("2026-09-17", 1, 1)
    )

    assertEquals(1, history.completionStreak("2026-09-19"))
  }
}
