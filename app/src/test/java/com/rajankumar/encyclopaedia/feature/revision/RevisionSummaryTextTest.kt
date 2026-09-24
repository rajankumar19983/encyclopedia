package com.rajankumar.encyclopaedia.feature.revision

import org.junit.Assert.assertEquals
import org.junit.Test

class RevisionSummaryTextTest {
  @Test fun accessibilitySummaryIncludesPriorityCounts() {
    assertEquals("8 questions need revision, including 2 urgent and 3 high priority.", RevisionStats(8, 2, 3, 1).accessibilitySummary())
  }
}
