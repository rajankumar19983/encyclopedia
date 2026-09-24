package com.rajankumar.encyclopaedia.feature.revision

import org.junit.Assert.assertEquals
import org.junit.Test

class RevisionPriorityLabelTest {
  @Test fun labelsPriorities() { assertEquals("Low priority", RevisionPriority.LOW.label()); assertEquals("Needs revision", RevisionPriority.MEDIUM.label()); assertEquals("High priority", RevisionPriority.HIGH.label()) }
}
