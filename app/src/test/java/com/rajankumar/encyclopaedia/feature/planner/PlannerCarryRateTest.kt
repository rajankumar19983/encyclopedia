package com.rajankumar.encyclopaedia.feature.planner

import com.rajankumar.encyclopaedia.data.local.PlannerTaskEntity
import org.junit.Assert.assertEquals
import org.junit.Test

class PlannerCarryRateTest {
  @Test
  fun rateMeasuresTasksOriginatingOnEarlierDays() {
    val tasks = listOf(task("1", "2026-09-18"), task("2", null), task("3", "2026-09-17"), task("4", null))
    assertEquals(50, tasks.carryOverRatePercent())
  }

  @Test
  fun emptyTasksHaveZeroCarryRate() {
    assertEquals(0, emptyList<PlannerTaskEntity>().carryOverRatePercent())
  }

  private fun task(id: String, carriedFrom: String?) = PlannerTaskEntity(
    id = id,
    title = id,
    scheduledDate = "2026-09-19",
    carriedFromDate = carriedFrom
  )
}
