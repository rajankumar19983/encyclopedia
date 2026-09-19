package com.rajankumar.encyclopaedia.feature.planner

import com.rajankumar.encyclopaedia.data.local.PlannerTaskEntity
import org.junit.Assert.assertEquals
import org.junit.Test

class PlannerLoadTest {
  @Test
  fun pendingLoadSeparatesCarriedAndNewTasks() {
    val tasks = listOf(task("done", true), task("carried", false, "2026-09-18"), task("new1"), task("new2"))
    val load = tasks.pendingLoad()
    assertEquals(3, load.totalPending)
    assertEquals(1, load.carriedPending)
    assertEquals(2, load.newPending)
  }

  private fun task(id: String, completed: Boolean = false, carriedFrom: String? = null) = PlannerTaskEntity(
    id = id,
    title = id,
    scheduledDate = "2026-09-19",
    isCompleted = completed,
    carriedFromDate = carriedFrom
  )
}
