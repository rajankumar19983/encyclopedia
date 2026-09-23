package com.rajankumar.encyclopaedia.feature.performance

import org.junit.Assert.assertEquals
import org.junit.Test

class RevisionSummaryTest {
  @Test fun countsPriorityBands() {
    val summary = revisionSummary(listOf(RevisionQueueItem("1", "A", 20, 2), RevisionQueueItem("2", "B", 60, 2), RevisionQueueItem("3", "C", 90, 5)))
    assertEquals(RevisionSummary(3, 1, 1, 1), summary)
  }
}
