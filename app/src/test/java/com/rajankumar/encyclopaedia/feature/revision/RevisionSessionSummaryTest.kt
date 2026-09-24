package com.rajankumar.encyclopaedia.feature.revision

import org.junit.Assert.assertEquals
import org.junit.Test

class RevisionSessionSummaryTest {
  @Test fun combinesRevisionState() = assertEquals("4 questions due • 2 topics need revision • 2 of 4 revised • 50%", RevisionSessionSummary(4, 2, RevisionSessionProgress(2, 4)).label())
}
