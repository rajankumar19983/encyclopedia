package com.rajankumar.encyclopaedia.feature.revision

import org.junit.Assert.assertEquals
import org.junit.Test

class RevisionSessionLabelTest {
  @Test fun labelsSessionStates() { assertEquals("No revision session", RevisionSessionProgress(0, 0).label()); assertEquals("3 of 6 revised • 50%", RevisionSessionProgress(3, 6).label()); assertEquals("Revision session complete", RevisionSessionProgress(6, 6).label()) }
}
