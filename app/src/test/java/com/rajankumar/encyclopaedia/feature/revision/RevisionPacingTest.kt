package com.rajankumar.encyclopaedia.feature.revision

import org.junit.Assert.assertEquals
import org.junit.Test

class RevisionPacingTest {
  @Test fun smallQueueUsesShortSession() = assertEquals(RevisionPace.SHORT, revisionPace(4))
  @Test fun mediumQueueUsesStandardSession() = assertEquals(RevisionPace.STANDARD, revisionPace(10))
  @Test fun backlogUsesFocusedSession() = assertEquals("Focused backlog session", revisionPace(25).label())
}
