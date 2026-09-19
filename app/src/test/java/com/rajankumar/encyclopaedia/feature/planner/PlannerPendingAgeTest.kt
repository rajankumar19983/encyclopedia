package com.rajankumar.encyclopaedia.feature.planner

import com.rajankumar.encyclopaedia.data.local.PlannerTaskEntity
import org.junit.Assert.assertEquals
import org.junit.Test

class PlannerPendingAgeTest {
  @Test
  fun ageUsesOldestOriginOfIncompleteTask() {
    val tasks = listOf(task("1", "2026-09-19", "2026-09-16"), task("2", "2026-09-19", null))
    assertEquals(3L, tasks.oldestPendingAgeDays("2026-09-19"))
  }

  @Test
  fun completedTasksDoNotAffectAge() {
    val tasks = listOf(task("1", "2026-09-19", "2026-09-10", true), task("2", "2026-09-19", null))
    assertEquals(0L, tasks.oldestPendingAgeDays("2026-09-19"))
  }

  private fun task(id: String, date: String, carriedFrom: String?, completed: Boolean = false) = PlannerTaskEntity(
    id = id,
    title = id,
    scheduledDate = date,
    carriedFromDate = carriedFrom,
    isCompleted = completed
  )
}
