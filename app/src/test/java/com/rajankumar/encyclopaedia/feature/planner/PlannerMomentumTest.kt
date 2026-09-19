package com.rajankumar.encyclopaedia.feature.planner

import org.junit.Assert.assertEquals
import org.junit.Test

class PlannerMomentumTest {
  @Test
  fun recentCompletionUsesLatestPlannedDays() {
    val history = (1..8).map { day ->
      val date = "2026-09-${day.toString().padStart(2, '0')}"
      PlannerDayHistory(date, if (day >= 3) 2 else 0, 2)
    }

    assertEquals(85, history.recentCompletionPercent(7))
  }

  @Test
  fun zeroWindowHasZeroMomentum() {
    assertEquals(0, listOf(PlannerDayHistory("2026-09-19", 1, 1)).recentCompletionPercent(0))
  }
}
