package com.rajankumar.encyclopaedia.feature.planner

import com.rajankumar.encyclopaedia.data.local.PlannerTaskEntity
import org.junit.Assert.assertEquals
import org.junit.Test

class PlannerCapacityTest {
  @Test
  fun suggestsRoomBelowTarget() {
    val tasks = listOf(PlannerTaskEntity("a", "A", "2026-09-22"), PlannerTaskEntity("b", "B", "2026-09-22"))
    assertEquals(3, tasks.plannerCapacity().suggestedAdditionalTasks)
  }

  @Test
  fun carriedBacklogBlocksAdditionalPlanning() {
    val tasks = (1..3).map { PlannerTaskEntity("$it", "Task $it", "2026-09-22", carriedFromDate = "2026-09-21") }
    assertEquals(0, tasks.plannerCapacity().suggestedAdditionalTasks)
  }
}
