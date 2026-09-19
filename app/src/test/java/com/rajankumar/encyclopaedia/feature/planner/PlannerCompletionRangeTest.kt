package com.rajankumar.encyclopaedia.feature.planner

import org.junit.Assert.assertEquals
import org.junit.Test

class PlannerCompletionRangeTest {
  @Test
  fun rangeFindsLowestAndHighestCompletion() {
    val range = listOf(
      PlannerDayHistory("2026-09-19", 1, 4),
      PlannerDayHistory("2026-09-18", 3, 4),
      PlannerDayHistory("2026-09-17", 2, 4)
    ).completionRange()
    assertEquals(25, range.lowestPercent)
    assertEquals(75, range.highestPercent)
  }

  @Test
  fun emptyHistoryReturnsZeroRange() {
    assertEquals(PlannerCompletionRange(0, 0), emptyList<PlannerDayHistory>().completionRange())
  }
}
