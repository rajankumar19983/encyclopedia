package com.rajankumar.encyclopaedia.feature.performance

import org.junit.Assert.assertEquals
import org.junit.Test

class PerformanceAccuracyGapTest {
  @Test fun returnsGapToTarget() = assertEquals(20, PerformanceSummary(10, 6, 5, 10, 0).accuracyGap(80))
  @Test fun returnsZeroWhenTargetMet() = assertEquals(0, PerformanceSummary(10, 9, 5, 10, 0).accuracyGap(80))
}
