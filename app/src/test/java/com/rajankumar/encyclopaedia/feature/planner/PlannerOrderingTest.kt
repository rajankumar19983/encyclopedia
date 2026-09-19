package com.rajankumar.encyclopaedia.feature.planner

import com.rajankumar.encyclopaedia.data.local.PlannerTaskEntity
import org.junit.Assert.assertEquals
import org.junit.Test

class PlannerOrderingTest {
  @Test
  fun incompleteCarriedTasksAppearBeforeNewAndCompletedTasks() {
    val tasks = listOf(
      task("done", completed = true, createdAt = 1),
      task("new", createdAt = 2),
      task("carried", carriedFrom = "2026-09-18", createdAt = 3)
    )

    assertEquals(listOf("carried", "new", "done"), tasks.orderedForPlanner().map { it.id })
  }

  private fun task(
    id: String,
    completed: Boolean = false,
    carriedFrom: String? = null,
    createdAt: Long
  ) = PlannerTaskEntity(
    id = id,
    title = id,
    scheduledDate = "2026-09-19",
    isCompleted = completed,
    carriedFromDate = carriedFrom,
    createdAt = createdAt
  )
}
