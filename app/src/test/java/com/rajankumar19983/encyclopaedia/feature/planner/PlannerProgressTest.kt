package com.rajankumar19983.encyclopaedia.feature.planner

import com.rajankumar19983.encyclopaedia.data.local.PlannerTaskEntity
import org.junit.Assert.assertEquals
import org.junit.Test

class PlannerProgressTest {
  @Test
  fun progressCountsCompletedAndRemainingTasks() {
    val tasks = listOf(
      task("1", true),
      task("2", false),
      task("3", true),
      task("4", false)
    )

    val progress = tasks.plannerProgress()

    assertEquals(2, progress.completed)
    assertEquals(4, progress.total)
    assertEquals(2, progress.remaining)
    assertEquals(50, progress.percent)
  }

  @Test
  fun emptyPlanHasZeroProgress() {
    val progress = emptyList<PlannerTaskEntity>().plannerProgress()

    assertEquals(0, progress.completed)
    assertEquals(0, progress.total)
    assertEquals(0, progress.remaining)
    assertEquals(0, progress.percent)
  }

  private fun task(id: String, completed: Boolean) = PlannerTaskEntity(
    id = id,
    title = "Task $id",
    scheduledDate = "2026-09-19",
    isCompleted = completed
  )
}
