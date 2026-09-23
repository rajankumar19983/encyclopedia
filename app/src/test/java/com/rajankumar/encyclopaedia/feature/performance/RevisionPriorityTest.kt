package com.rajankumar.encyclopaedia.feature.performance

import org.junit.Assert.assertEquals
import org.junit.Test

class RevisionPriorityTest {
  @Test fun prioritisesUnseenAndWeakMaterial() {
    assertEquals(RevisionPriority.HIGH, revisionPriority(0, 0))
    assertEquals(RevisionPriority.HIGH, revisionPriority(40, 5))
    assertEquals(RevisionPriority.MEDIUM, revisionPriority(60, 5))
    assertEquals(RevisionPriority.LOW, revisionPriority(80, 5))
  }
}
