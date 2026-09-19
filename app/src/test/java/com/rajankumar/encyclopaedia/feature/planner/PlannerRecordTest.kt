package com.rajankumar.encyclopaedia.feature.planner

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class PlannerRecordTest {
  @Test
  fun busiestDayUsesLargestPlan() {
    val history = listOf(
      PlannerDayHistory("2026-09-17", 2, 3),
      PlannerDayHistory("2026-09-18", 3, 6),
      PlannerDayHistory("2026-09-19", 4, 5)
    )

    assertEquals("2026-09-18", history.busiestPlannerDay()?.date)
  }

  @Test
  fun emptyHistoryHasNoBusiestDay() {
    assertNull(emptyList<PlannerDayHistory>().busiestPlannerDay())
  }
}
