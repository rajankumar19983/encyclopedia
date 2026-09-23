package com.rajankumar.encyclopaedia.feature.performance

import org.junit.Assert.assertEquals
import org.junit.Test

class RevisionReasonTest {
  @Test fun explainsRevisionNeed() {
    assertEquals("Not practised yet", revisionReason(0, 0))
    assertEquals("Frequent mistakes", revisionReason(40, 3))
    assertEquals("Needs reinforcement", revisionReason(60, 3))
    assertEquals("Keep fresh", revisionReason(90, 3))
  }
}
