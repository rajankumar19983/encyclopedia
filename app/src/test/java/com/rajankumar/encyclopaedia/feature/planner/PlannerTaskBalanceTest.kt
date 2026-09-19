package com.rajankumar.encyclopaedia.feature.planner

import org.junit.Assert.assertEquals
import org.junit.Test

class PlannerTaskBalanceTest {
  @Test
  fun balanceAggregatesCompletedAndPendingTasks() {
    val balance = listOf(
      PlannerDayHistory("2026-09-19", 3, 4),
      PlannerDayHistory("2026-09-18", 2, 4)
    ).taskBalance()
    assertEquals(5, balance.completed)
    assertEquals(3, balance.pending)
    assertEquals(62, balance.completionPercent)
  }
}
