package com.rajankumar.encyclopaedia.feature.planner

import com.rajankumar.encyclopaedia.data.local.PlannerTaskEntity
import org.junit.Assert.assertEquals
import org.junit.Test

class PlannerBacklogTest {
  @Test
  fun pressureMeasuresCarriedShareOfPendingWork() {
    val tasks = listOf(task("1", false, "2026-09-18"), task("2", false, null), task("3", false, "2026-09-17"), task("4", true, "2026-09-16"))
    assertEquals(66, tasks.backlogPressurePercent())
  }

  @Test
  fun noPendingTasksHasZeroPressure() {
    assertEquals(0, listOf(task("1", true, "2026-09-18")).backlogPressurePercent())
  }

  private fun task(id: String, completed: Boolean, carriedFrom: String?) = PlannerTaskEntity(
    id = id,
    title = id,
    scheduledDate = "2026-09-19",
    isCompleted = completed,
    carriedFromDate = carriedFrom
  )
}
