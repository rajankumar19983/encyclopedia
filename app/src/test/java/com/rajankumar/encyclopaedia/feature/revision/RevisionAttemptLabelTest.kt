package com.rajankumar.encyclopaedia.feature.revision

import org.junit.Assert.assertEquals
import org.junit.Test

class RevisionAttemptLabelTest {
  @Test fun labelsAttempts() { assertEquals("Not attempted yet", revisionAttemptLabel(0)); assertEquals("1 revision attempt", revisionAttemptLabel(1)); assertEquals("4 revision attempts", revisionAttemptLabel(4)) }
}
