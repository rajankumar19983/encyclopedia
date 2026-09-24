package com.rajankumar.encyclopaedia.feature.revision

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class RevisionPriorityCountsTest {
  @Test fun countSummaryExposesTotal() {
    val counts = RevisionPriorityCounts(1, 2, 3, 4)
    assertEquals(10, counts.total)
    assertTrue(counts.needsImmediateAttention)
  }
}
