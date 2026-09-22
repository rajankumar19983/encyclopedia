package com.rajankumar.encyclopaedia.feature.planner

import com.rajankumar.encyclopaedia.data.local.PlannerTaskEntity
import org.junit.Assert.assertEquals
import org.junit.Test

class PlannerPriorityTest {
  @Test
  fun carriedTasksComeBeforeNewTasksAndOldestCarryComesFirst() {
    val tasks = listOf(
      PlannerTaskEntity("new", "New", "2026-09-22"),
      PlannerTaskEntity("recent", "Recent", "2026-09-22", carriedFromDate = "2026-09-21"),
      PlannerTaskEntity("old", "Old", "2026-09-22", carriedFromDate = "2026-09-19"),
    )
    assertEquals(listOf("old", "recent", "new"), tasks.plannerPriorities().map { it.task.id })
  }

  @Test
  fun completedTasksAreNeverPriorities() {
    val tasks = listOf(PlannerTaskEntity("done", "Done", "2026-09-22", isCompleted = true))
    assertEquals(emptyList<PlannerPriorityItem>(), tasks.plannerPriorities())
  }
}
