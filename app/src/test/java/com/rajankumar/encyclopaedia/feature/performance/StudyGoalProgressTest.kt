package com.rajankumar.encyclopaedia.feature.performance

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class StudyGoalProgressTest {
  @Test fun reportsRemainingWork() {
    val progress = StudyGoalProgress(8, 20)
    assertEquals(40, progress.percent)
    assertEquals(12, progress.remaining)
    assertFalse(progress.isComplete)
  }

  @Test fun capsCompletedGoalAtHundredPercent() {
    val progress = StudyGoalProgress(24, 20)
    assertEquals(100, progress.percent)
    assertTrue(progress.isComplete)
  }
}
