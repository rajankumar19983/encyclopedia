package com.rajankumar.encyclopaedia.feature.performance

import org.junit.Assert.assertEquals
import org.junit.Test

class RevisionQueueItemTest {
  @Test fun derivesRevisionMetadata() {
    val item = RevisionQueueItem("q1", "Binary trees", 40, 3)
    assertEquals(RevisionPriority.HIGH, item.priority)
    assertEquals("Frequent mistakes", item.reason)
  }
}
