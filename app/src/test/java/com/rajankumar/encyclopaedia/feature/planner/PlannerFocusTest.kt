package com.rajankumar.encyclopaedia.feature.planner

import com.rajankumar.encyclopaedia.data.local.PlannerTaskEntity
import org.junit.Assert.assertEquals
import org.junit.Test

class PlannerFocusTest {
  @Test
  fun separatesCarriedAndNewPendingWork() {
    val tasks = listOf(
      PlannerTaskEntity("a", "Old", "2026-09-22", carriedFromDate = "2026-09-21"),
      PlannerTaskEntity("b", "New", "2026-09-22"),
      PlannerTaskEntity("c", "Done", "2026-09-22", isCompleted = true),
    )
    val focus = tasks.plannerFocus()
    assertEquals(3, focus.total)
    assertEquals(2, focus.pending)
    assertEquals(1, focus.carriedPending)
    assertEquals(1, focus.newPending)
    assertEquals(33, focus.completionPercent)
  }

  @Test
  fun completedPlanHasCompletionHeadline() {
    val focus = listOf(PlannerTaskEntity("a", "Done", "2026-09-22", isCompleted = true)).plannerFocus()
    assertEquals("Today's plan is complete", focus.headline())
  }
}
