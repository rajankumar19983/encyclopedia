package com.rajankumar.encyclopaedia.feature.planner

import org.junit.Assert.assertEquals
import org.junit.Test

class PlannerZeroDaysTest {
  @Test
  fun countsPlannedDaysWithNothingCompleted() {
    val history = listOf(
      PlannerDayHistory("2026-09-19", 0, 3),
      PlannerDayHistory("2026-09-18", 1, 3),
      PlannerDayHistory("2026-09-17", 0, 2)
    )
    assertEquals(2, history.zeroCompletionDays())
  }

  @Test
  fun emptyHistoryHasNoZeroDays() {
    assertEquals(0, emptyList<PlannerDayHistory>().zeroCompletionDays())
  }
}
