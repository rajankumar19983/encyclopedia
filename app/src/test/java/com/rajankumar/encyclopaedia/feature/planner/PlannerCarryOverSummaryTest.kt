package com.rajankumar.encyclopaedia.feature.planner

import com.rajankumar.encyclopaedia.data.local.PlannerTaskEntity
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class PlannerCarryOverSummaryTest {
  @Test
  fun summaryCountsIncompleteCarriedTasksAndOldestDate() {
    val tasks = listOf(
      task("1", "2026-09-18"),
      task("2", "2026-09-16"),
      task("3", null),
      task("4", "2026-09-15", completed = true)
    )

    val summary = tasks.carryOverSummary()

    assertEquals(2, summary.carriedCount)
    assertEquals("2026-09-16", summary.oldestSourceDate)
  }

  @Test
  fun summaryIsEmptyWhenNothingWasCarried() {
    val summary = listOf(task("1", null)).carryOverSummary()

    assertEquals(0, summary.carriedCount)
    assertNull(summary.oldestSourceDate)
  }

  private fun task(id: String, carriedFrom: String?, completed: Boolean = false) = PlannerTaskEntity(
    id = id,
    title = id,
    scheduledDate = "2026-09-19",
    isCompleted = completed,
    carriedFromDate = carriedFrom
  )
}
