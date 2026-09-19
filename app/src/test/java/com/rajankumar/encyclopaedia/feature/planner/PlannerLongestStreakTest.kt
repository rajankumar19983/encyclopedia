package com.rajankumar.encyclopaedia.feature.planner

import org.junit.Assert.assertEquals
import org.junit.Test

class PlannerLongestStreakTest {
  @Test
  fun findsLongestCompletedRunAnywhereInHistory() {
    val history = listOf(
      day("2026-09-10", true), day("2026-09-11", true), day("2026-09-12", false),
      day("2026-09-15", true), day("2026-09-16", true), day("2026-09-17", true)
    )
    assertEquals(3, history.longestCompletionStreak())
  }

  @Test
  fun noCompletedDaysHasZeroLongestStreak() {
    assertEquals(0, listOf(day("2026-09-19", false)).longestCompletionStreak())
  }

  private fun day(date: String, complete: Boolean) = PlannerDayHistory(date, if (complete) 1 else 0, 1)
}
