package com.rajankumar.encyclopaedia.feature.revision

import org.junit.Assert.assertEquals
import org.junit.Test

class RevisionQueueStatusTest {
  @Test fun derivesQueueStatus() { assertEquals(RevisionQueueStatus.CLEAR, revisionQueueStatus(0)); assertEquals(RevisionQueueStatus.READY, revisionQueueStatus(5)); assertEquals(RevisionQueueStatus.BUSY, revisionQueueStatus(10)) }
}
