package com.rajankumar.encyclopaedia.feature.revision

import org.junit.Assert.assertEquals
import org.junit.Test

class RevisionSessionSizeTest {
  @Test fun smallQueueFitsSession() = assertEquals(8, suggestedRevisionSessionSize(8))
  @Test fun largeQueueIsCapped() = assertEquals(20, suggestedRevisionSessionSize(40))
  @Test fun invalidQueueIsSafe() = assertEquals(0, suggestedRevisionSessionSize(-3))
}
