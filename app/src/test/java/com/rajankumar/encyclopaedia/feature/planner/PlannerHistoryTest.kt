package com.rajankumar.encyclopaedia.feature.planner

import com.rajankumar.encyclopaedia.data.local.PlannerTaskEntity
import org.junit.Assert.assertEquals
import org.junit.Test

class PlannerHistoryTest {
  @Test
  fun historyGroupsTasksByDateNewestFirst() {
    val tasks = listOf(
      task("1", "2026-09-18", true),
      task("2", "2026-09-18", false),
      task("3", "2026-09-19", true),
      task("4", "2026-09-19", true)
    )

    val history = tasks.plannerHistory()

    assertEquals(listOf("2026-09-19", "2026-09-18"), history.map { it.date })
    assertEquals(2, history[0].completed)
    assertEquals(2, history[0].total)
    assertEquals(100, history[0].percent)
    assertEquals(1, history[1].completed)
    assertEquals(50, history[1].percent)
  }

  private fun task(id: String, date: String, completed: Boolean) = PlannerTaskEntity(
    id = id,
    title = id,
    scheduledDate = date,
    isCompleted = completed
  )
}
