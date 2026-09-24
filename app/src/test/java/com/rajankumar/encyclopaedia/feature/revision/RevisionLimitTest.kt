package com.rajankumar.encyclopaedia.feature.revision

import org.junit.Assert.assertEquals
import org.junit.Test

class RevisionLimitTest {
  @Test fun emptyQueueHasNoSession() = assertEquals("No session ready", revisionSessionLimitLabel(0))
  @Test fun smallQueueUsesItsSize() = assertEquals("4 questions recommended", revisionSessionLimitLabel(4))
}
