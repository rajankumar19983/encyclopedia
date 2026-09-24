package com.rajankumar.encyclopaedia.feature.revision

import org.junit.Assert.assertEquals
import org.junit.Test

class RevisionReasonsSummaryTest {
  @Test fun emptyReasonsHaveReadableSummary() = assertEquals("No revision reason recorded", emptySet<RevisionReason>().summary())
  @Test fun reasonsUseReadableLabels() = assertEquals("Answered incorrectly • Slow answer", linkedSetOf(RevisionReason.INCORRECT, RevisionReason.SLOW_ANSWER).summary())
}
