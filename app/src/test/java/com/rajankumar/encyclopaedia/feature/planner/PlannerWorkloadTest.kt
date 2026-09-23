package com.rajankumar.encyclopaedia.feature.planner

import com.rajankumar.encyclopaedia.data.local.PlannerTaskEntity
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class PlannerWorkloadTest {
  @Test
  fun heavyCarriedBacklogRecommendsPrioritisingCarryOver() {
    val tasks = (1..6).map { PlannerTaskEntity("$it", "Task $it", "2026-09-22", carriedFromDate = if (it <= 3) "2026-09-21" else null) }
    val workload = tasks.plannerWorkload()
    assertEquals(PlannerWorkloadLevel.HEAVY, workload.level)
    assertEquals(3, workload.carried)
    assertTrue(workload.guidance().contains("carried"))
  }

  @Test
  fun emptyPlannerGetsPlanningPrompt() {
    assertEquals(PlannerWorkloadLevel.EMPTY, emptyList<PlannerTaskEntity>().plannerWorkload().level)
  }
}
