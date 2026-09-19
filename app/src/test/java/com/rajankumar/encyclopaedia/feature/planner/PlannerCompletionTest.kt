package com.rajankumar.encyclopaedia.feature.planner

import com.rajankumar.encyclopaedia.data.local.PlannerTaskEntity
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class PlannerCompletionTest {
  @Test
  fun completingTaskRecordsCompletionTime() {
    val completed = task("1").withCompletion(true, now = 500L)

    assertTrue(completed.isCompleted)
    assertEquals(500L, completed.completedAt)
    assertEquals(500L, completed.updatedAt)
  }

  @Test
  fun reopeningTaskClearsCompletionTime() {
    val reopened = task("1", completed = true, completedAt = 100L).withCompletion(false, now = 600L)

    assertFalse(reopened.isCompleted)
    assertNull(reopened.completedAt)
    assertEquals(600L, reopened.updatedAt)
  }

  @Test
  fun allCompleteRequiresAtLeastOneTask() {
    assertFalse(emptyList<PlannerTaskEntity>().allPlannerTasksComplete())
    assertTrue(listOf(task("1", true, 100L), task("2", true, 200L)).allPlannerTasksComplete())
    assertFalse(listOf(task("1", true, 100L), task("2")).allPlannerTasksComplete())
  }

  private fun task(id: String, completed: Boolean = false, completedAt: Long? = null) = PlannerTaskEntity(
    id = id,
    title = id,
    scheduledDate = "2026-09-19",
    isCompleted = completed,
    completedAt = completedAt
  )
}
