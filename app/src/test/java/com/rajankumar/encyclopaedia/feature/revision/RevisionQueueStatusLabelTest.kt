package com.rajankumar.encyclopaedia.feature.revision

import org.junit.Assert.assertEquals
import org.junit.Test

class RevisionQueueStatusLabelTest {
  @Test fun labelsQueueStates() { assertEquals("Queue clear", RevisionQueueStatus.CLEAR.label()); assertEquals("Revision ready", RevisionQueueStatus.READY.label()); assertEquals("Revision backlog", RevisionQueueStatus.BUSY.label()) }
}
