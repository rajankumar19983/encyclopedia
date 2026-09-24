package com.rajankumar.encyclopaedia.feature.revision

import org.junit.Assert.assertEquals
import org.junit.Test

class RevisionDueLabelTest {
  @Test fun labelsDueCounts() { assertEquals("Nothing due for revision", revisionDueLabel(0)); assertEquals("1 question due", revisionDueLabel(1)); assertEquals("8 questions due", revisionDueLabel(8)) }
}
