package com.rajankumar.encyclopaedia.feature.revision

import org.junit.Assert.assertEquals
import org.junit.Test

class RevisionPriorityTest {
  @Test fun derivesPriority() { assertEquals(RevisionPriority.LOW, revisionPriority(0)); assertEquals(RevisionPriority.MEDIUM, revisionPriority(2)); assertEquals(RevisionPriority.HIGH, revisionPriority(3)) }
}
