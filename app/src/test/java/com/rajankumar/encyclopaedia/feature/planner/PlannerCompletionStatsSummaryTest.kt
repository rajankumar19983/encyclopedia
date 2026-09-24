package com.rajankumar.encyclopaedia.feature.planner

import org.junit.Assert.assertEquals
import org.junit.Test

class PlannerCompletionStatsSummaryTest {
  @Test fun summaryCombinesPendingGroups() {
    val stats = PlannerCompletionStats(completedToday = 2, carriedPending = 1, newlyPlannedPending = 3)
    assertEquals(4, stats.pending)
    assertEquals(6, stats.total)
    assertEquals("Completed 2 • Pending 4", stats.summary)
  }
}
