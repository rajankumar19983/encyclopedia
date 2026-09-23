package com.rajankumar.encyclopaedia.feature.performance

import org.junit.Assert.assertEquals
import org.junit.Test

class RevisionQueueLimitTest {
  @Test fun sessionRespectsLimit() {
    val items = (1..8).map { RevisionQueueItem("$it", "Q$it", it * 10, 2) }
    assertEquals(3, items.takeRevisionSession(3).size)
    assertEquals(0, items.takeRevisionSession(-1).size)
  }
}
