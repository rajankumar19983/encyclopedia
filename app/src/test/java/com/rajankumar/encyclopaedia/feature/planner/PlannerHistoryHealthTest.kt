package com.rajankumar.encyclopaedia.feature.planner

import org.junit.Assert.assertEquals
import org.junit.Test

class PlannerHistoryHealthTest {
  @Test fun emptyHistoryHasNoDataHealth() = assertEquals(PlannerHistoryHealth.NO_DATA, emptyList<PlannerDayHistory>().plannerHistoryHealth())

  @Test
  fun lowRecentCompletionIsStruggling() {
    val history = listOf(PlannerDayHistory("2026-09-22", 1, 4), PlannerDayHistory("2026-09-21", 1, 4))
    assertEquals(PlannerHistoryHealth.STRUGGLING, history.plannerHistoryHealth())
  }

  @Test
  fun reliableCompletionIsStrong() {
    val history = listOf(PlannerDayHistory("2026-09-22", 4, 4), PlannerDayHistory("2026-09-21", 4, 4))
    assertEquals(PlannerHistoryHealth.STRONG, history.plannerHistoryHealth())
  }
}
