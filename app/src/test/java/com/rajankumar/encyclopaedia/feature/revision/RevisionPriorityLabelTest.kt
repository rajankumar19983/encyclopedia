package com.rajankumar.encyclopaedia.feature.revision

import org.junit.Assert.assertEquals
import org.junit.Test

class RevisionPriorityLabelTest {
  @Test fun labelsPriorities() {
    assertEquals("Urgent priority", RevisionPriority.URGENT.displayLabel())
    assertEquals("High priority", RevisionPriority.HIGH.displayLabel())
    assertEquals("Normal priority", RevisionPriority.NORMAL.displayLabel())
    assertEquals("Low priority", RevisionPriority.LOW.displayLabel())
  }
}
