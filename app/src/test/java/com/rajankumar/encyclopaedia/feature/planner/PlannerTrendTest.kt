package com.rajankumar.encyclopaedia.feature.planner

import org.junit.Assert.assertEquals
import org.junit.Test

class PlannerTrendTest {
  @Test
  fun detectsImprovingTrend() {
    val history = listOf(
      day("2026-09-19", 100), day("2026-09-18", 80), day("2026-09-17", 60),
      day("2026-09-16", 40), day("2026-09-15", 20), day("2026-09-14", 0)
    )
    assertEquals(PlannerTrend.IMPROVING, history.completionTrend())
  }

  @Test
  fun detectsDecliningAndSteadyTrends() {
    val declining = listOf(day("2026-09-19", 20), day("2026-09-18", 20), day("2026-09-17", 20), day("2026-09-16", 80), day("2026-09-15", 80), day("2026-09-14", 80))
    val steady = List(6) { index -> day("2026-09-${19 - index}", 50) }
    assertEquals(PlannerTrend.DECLINING, declining.completionTrend())
    assertEquals(PlannerTrend.STEADY, steady.completionTrend())
  }

  @Test
  fun requiresTwoFullWindows() {
    assertEquals(PlannerTrend.INSUFFICIENT_DATA, listOf(day("2026-09-19", 100)).completionTrend())
  }

  private fun day(date: String, percent: Int) = PlannerDayHistory(date, percent, 100)
}
