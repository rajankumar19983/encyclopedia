package com.rajankumar.encyclopaedia.feature.planner

import org.junit.Assert.assertEquals
import org.junit.Test

class PlannerActiveDaysTest {
  @Test fun spanIncludesFirstAndLastPlannerDate() {
    val history = listOf(PlannerDayHistory("2026-09-15", 1, 1), PlannerDayHistory("2026-09-19", 1, 2))
    assertEquals(5L, history.activeDaySpan())
  }
  @Test fun emptyHistoryHasZeroSpan() = assertEquals(0L, emptyList<PlannerDayHistory>().activeDaySpan())
  @Test fun invalidDatesAreIgnored() {
    val history = listOf(PlannerDayHistory("bad", 1, 1), PlannerDayHistory("2026-09-19", 1, 2))
    assertEquals(1L, history.activeDaySpan())
  }
}
