package com.rajankumar.encyclopaedia.feature.planner

import com.rajankumar.encyclopaedia.data.local.PlannerTaskEntity
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class PlannerNextActionTest {
  @Test
  fun selectsOldestCarriedPendingTask() {
    val tasks = listOf(
      PlannerTaskEntity("new", "New task", "2026-09-22"),
      PlannerTaskEntity("old", "Old task", "2026-09-22", carriedFromDate = "2026-09-18"),
    )
    assertEquals("old", tasks.plannerNextAction()?.taskId)
  }

  @Test
  fun returnsNullWhenNothingIsPending() {
    val tasks = listOf(PlannerTaskEntity("done", "Done", "2026-09-22", isCompleted = true))
    assertNull(tasks.plannerNextAction())
  }
}
