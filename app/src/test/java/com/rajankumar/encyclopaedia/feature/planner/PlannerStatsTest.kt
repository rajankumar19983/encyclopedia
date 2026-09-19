package com.rajankumar.encyclopaedia.feature.planner

import com.rajankumar.encyclopaedia.data.local.PlannerTaskEntity
import org.junit.Assert.assertEquals
import org.junit.Test

class PlannerStatsTest {
  @Test
  fun statsSeparateCompletedCarriedAndNewPendingTasks() {
    val tasks = listOf(
      task("done", completed = true),
      task("carried", carriedFrom = "2026-09-18"),
      task("new1"),
      task("new2")
    )

    val stats = tasks.completionStats()

    assertEquals(1, stats.completedToday)
    assertEquals(1, stats.carriedPending)
    assertEquals(2, stats.newlyPlannedPending)
  }

  private fun task(
    id: String,
    completed: Boolean = false,
    carriedFrom: String? = null
  ) = PlannerTaskEntity(
    id = id,
    title = id,
    scheduledDate = "2026-09-19",
    isCompleted = completed,
    carriedFromDate = carriedFrom
  )
}
