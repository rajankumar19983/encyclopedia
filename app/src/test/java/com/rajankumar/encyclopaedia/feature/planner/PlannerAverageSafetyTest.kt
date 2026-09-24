package com.rajankumar.encyclopaedia.feature.planner

import org.junit.Assert.assertEquals
import org.junit.Test

class PlannerAverageSafetyTest {
  @Test fun negativeTotalsDoNotReduceAverage() {
    val history = listOf(PlannerDayHistory("2026-09-20", 0, -4), PlannerDayHistory("2026-09-21", 2, 4))
    assertEquals(2.0, history.averageTasksPerPlannedDay(), 0.0)
  }
}
