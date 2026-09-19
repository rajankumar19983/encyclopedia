package com.rajankumar.encyclopaedia.feature.planner

import com.rajankumar.encyclopaedia.data.local.PlannerTaskEntity
import org.junit.Assert.assertEquals
import org.junit.Test

class PlannerRecoveryTest {
  @Test
  fun recoveryMeasuresCompletedCarriedTasks() {
    val tasks = listOf(task("1", true, "2026-09-17"), task("2", false, "2026-09-18"), task("3", true, null))
    assertEquals(50, tasks.carriedCompletionPercent())
  }

  @Test
  fun noCarriedTasksHasZeroRecovery() {
    assertEquals(0, listOf(task("1", true, null)).carriedCompletionPercent())
  }

  private fun task(id: String, completed: Boolean, carriedFrom: String?) = PlannerTaskEntity(
    id = id,
    title = id,
    scheduledDate = "2026-09-19",
    isCompleted = completed,
    carriedFromDate = carriedFrom
  )
}
