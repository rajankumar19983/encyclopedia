package com.rajankumar.encyclopaedia.feature.home

import org.junit.Assert.assertEquals
import org.junit.Test

class HomeTaskSummaryTest {
  @Test fun combinesPendingAndCompleted() {
    val summary = HomeTaskSummary(3, 2)
    assertEquals(5, summary.total)
    assertEquals("2 of 5 completed", summary.progress.label)
  }
}
