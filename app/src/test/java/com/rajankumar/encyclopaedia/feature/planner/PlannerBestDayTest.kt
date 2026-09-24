package com.rajankumar.encyclopaedia.feature.planner

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class PlannerBestDayTest {
  @Test fun bestDayPrefersCompletionRateThenCompletedCount() {
    val history = listOf(PlannerDayHistory("2026-09-17", 4, 5), PlannerDayHistory("2026-09-18", 2, 2), PlannerDayHistory("2026-09-19", 5, 5))
    assertEquals("2026-09-19", history.bestPlannerDay()?.date)
  }
  @Test fun tieUsesEarlierDatePredictably() {
    val history = listOf(PlannerDayHistory("2026-09-19", 2, 2), PlannerDayHistory("2026-09-18", 2, 2))
    assertEquals("2026-09-18", history.bestPlannerDay()?.date)
  }
  @Test fun emptyHistoryHasNoBestDay() = assertNull(emptyList<PlannerDayHistory>().bestPlannerDay())
}
