package com.rajankumar.encyclopaedia.feature.revision

import org.junit.Assert.assertEquals
import org.junit.Test

class RevisionCompletionMessageTest {
  @Test fun handlesEmptySession() = assertEquals("No questions revised", revisionCompletionMessage(0, 0))
  @Test fun summarizesCompletedSession() = assertEquals("Revision complete: 4 of 5 correct", revisionCompletionMessage(4, 5))
}
