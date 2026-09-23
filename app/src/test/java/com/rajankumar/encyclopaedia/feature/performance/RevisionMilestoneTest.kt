package com.rajankumar.encyclopaedia.feature.performance

import org.junit.Assert.assertTrue
import org.junit.Test

class RevisionMilestoneTest {
  @Test fun revisionFoundationIsComplete() = assertTrue(revisionMilestone.complete)
}
