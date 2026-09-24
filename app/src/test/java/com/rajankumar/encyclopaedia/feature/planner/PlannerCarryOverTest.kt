package com.rajankumar.encyclopaedia.feature.planner

import com.rajankumar.encyclopaedia.data.local.PlannerTaskEntity
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class PlannerCarryOverTest {
  @Test fun incompleteOlderTasksMoveToTargetDate() {
    val task = PlannerTaskEntity(id = "1", title = "Revise DBMS", scheduledDate = "2026-09-18")
    val carried = carryIncompleteTasks(listOf(task), "2026-09-19", now = 100L)
    assertEquals(1, carried.size)
    assertEquals("2026-09-19", carried.first().scheduledDate)
    assertEquals("2026-09-18", carried.first().carriedFromDate)
    assertEquals(100L, carried.first().updatedAt)
  }

  @Test fun completedAndFutureTasksAreNotCarried() {
    val tasks = listOf(PlannerTaskEntity(id = "done", title = "Done", scheduledDate = "2026-09-18", isCompleted = true), PlannerTaskEntity(id = "future", title = "Future", scheduledDate = "2026-09-20"))
    assertTrue(carryIncompleteTasks(tasks, "2026-09-19").isEmpty())
  }

  @Test fun originalCarryDateIsPreservedAcrossMultipleDays() {
    val task = PlannerTaskEntity(id = "1", title = "Revise OS", scheduledDate = "2026-09-18", carriedFromDate = "2026-09-17")
    assertEquals("2026-09-17", carryIncompleteTasks(listOf(task), "2026-09-19").first().carriedFromDate)
  }

  @Test fun blankTargetDoesNotMoveTasks() {
    val task = PlannerTaskEntity(id = "1", title = "Revise OS", scheduledDate = "2026-09-18")
    assertTrue(carryIncompleteTasks(listOf(task), "   ").isEmpty())
  }
}
