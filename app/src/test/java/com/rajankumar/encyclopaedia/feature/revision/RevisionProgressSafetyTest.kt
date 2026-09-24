package com.rajankumar.encyclopaedia.feature.revision

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class RevisionProgressSafetyTest {
  @Test fun completedIsClampedToTotal() {
    val progress = RevisionProgress(8, 3)
    assertEquals(3, progress.safeCompleted)
    assertEquals(100, progress.percent)
    assertTrue(progress.isComplete)
  }
  @Test fun negativeTotalBecomesZero() = assertEquals(0, RevisionProgress(2, -1).safeTotal)
}
