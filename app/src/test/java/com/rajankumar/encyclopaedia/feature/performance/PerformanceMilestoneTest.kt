package com.rajankumar.encyclopaedia.feature.performance

import org.junit.Assert.assertTrue
import org.junit.Test

class PerformanceMilestoneTest {
  @Test fun analyticsMilestoneIsComplete() = assertTrue(currentPerformanceMilestone.complete)
}
