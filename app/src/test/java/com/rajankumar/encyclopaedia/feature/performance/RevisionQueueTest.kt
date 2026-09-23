package com.rajankumar.encyclopaedia.feature.performance

import org.junit.Assert.assertEquals
import org.junit.Test

class RevisionQueueTest {
  @Test fun weakestItemComesFirst() {
    val queue = buildRevisionQueue(listOf(RevisionQueueItem("1", "Strong", 90, 5), RevisionQueueItem("2", "Weak", 30, 2)))
    assertEquals("Weak", queue.first().title)
  }
}
