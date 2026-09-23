package com.rajankumar.encyclopaedia.feature.planner

import com.rajankumar.encyclopaedia.data.local.PlannerTaskEntity
import org.junit.Assert.assertEquals
import org.junit.Test

class PlannerBacklogHealthTest {
  @Test
  fun noPendingTasksMeansClearBacklog() {
    val tasks = listOf(PlannerTaskEntity("done", "Done", "2026-09-22", isCompleted = true))
    assertEquals(PlannerBacklogHealth.CLEAR, tasks.backlogHealth())
  }

  @Test
  fun largeCarriedBacklogIsOverloaded() {
    val tasks = (1..7).map { PlannerTaskEntity("$it", "Task $it", "2026-09-22", carriedFromDate = "2026-09-20") }
    assertEquals(PlannerBacklogHealth.OVERLOADED, tasks.backlogHealth())
  }
}
