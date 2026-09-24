package com.rajankumar.encyclopaedia.feature.revision

import org.junit.Assert.assertEquals
import org.junit.Test

class RevisionReasonSummaryTest {
  @Test fun emptyReasonsUseFallback() = assertEquals("Needs revision", emptySet<RevisionReason>().revisionReasonSummary())
  @Test fun oneReasonUsesItsLabel() = assertEquals("Answered incorrectly", setOf(RevisionReason.INCORRECT).revisionReasonSummary())
}
