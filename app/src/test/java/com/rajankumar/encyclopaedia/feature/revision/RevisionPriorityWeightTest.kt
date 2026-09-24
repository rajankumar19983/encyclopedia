package com.rajankumar.encyclopaedia.feature.revision

import org.junit.Assert.assertTrue
import org.junit.Test

class RevisionPriorityWeightTest {
  @Test fun urgentOutweighsOtherPriorities() {
    assertTrue(RevisionPriority.URGENT.weight() > RevisionPriority.HIGH.weight())
    assertTrue(RevisionPriority.HIGH.weight() > RevisionPriority.NORMAL.weight())
  }
}
