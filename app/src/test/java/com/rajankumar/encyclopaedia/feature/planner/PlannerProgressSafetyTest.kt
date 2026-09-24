package com.rajankumar.encyclopaedia.feature.planner

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class PlannerProgressSafetyTest {
  @Test fun completedCountIsClampedToTotal() {
    val progress = PlannerProgress(7, 3)
    assertEquals(3, progress.safeCompleted)
    assertEquals(100, progress.percent)
    assertTrue(progress.isComplete)
  }

  @Test fun emptyProgressHasReadableSummary() = assertEquals("No tasks planned today", PlannerProgress(0, 0).accessibilitySummary)
}
