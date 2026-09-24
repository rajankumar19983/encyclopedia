package com.rajankumar.encyclopaedia.feature.revision

import org.junit.Assert.assertEquals
import org.junit.Test

class RevisionSessionPlanTest {
  @Test fun planTracksDeferredQuestions() {
    val plan = revisionSessionPlan(30, 20)
    assertEquals(20, plan.sessionSize)
    assertEquals(10, plan.deferred)
  }
  @Test fun emptyPlanHasClearSummary() = assertEquals("No revision due", revisionSessionPlan(0).summary)
}
