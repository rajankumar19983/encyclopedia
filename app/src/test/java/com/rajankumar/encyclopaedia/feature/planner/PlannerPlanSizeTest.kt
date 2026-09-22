package com.rajankumar.encyclopaedia.feature.planner

import org.junit.Assert.assertTrue
import org.junit.Test

class PlannerPlanSizeTest {
  @Test
  fun detectsRecentPlanGrowth() {
    val history = listOf(
      PlannerDayHistory("2026-09-22", 5, 6),
      PlannerDayHistory("2026-09-21", 4, 6),
      PlannerDayHistory("2026-09-20", 4, 6),
      PlannerDayHistory("2026-09-19", 5, 6),
      PlannerDayHistory("2026-09-18", 4, 6),
      PlannerDayHistory("2026-09-17", 5, 6),
      PlannerDayHistory("2026-09-16", 4, 6),
      PlannerDayHistory("2026-09-01", 2, 2),
      PlannerDayHistory("2026-08-31", 2, 2),
      PlannerDayHistory("2026-08-30", 2, 2),
      PlannerDayHistory("2026-08-29", 2, 2),
      PlannerDayHistory("2026-08-28", 2, 2),
      PlannerDayHistory("2026-08-27", 2, 2),
      PlannerDayHistory("2026-08-26", 2, 2),
    )
    assertTrue(history.planSizeComparison().message().contains("larger"))
  }
}
