package com.rajankumar.encyclopaedia.feature.performance

import org.junit.Assert.assertEquals
import org.junit.Test

class PerformanceSpeedLabelTest {
  @Test fun labelsTimingBands() {
    assertEquals("No timing data", averageSpeedLabel(0)); assertEquals("Fast", averageSpeedLabel(10_000)); assertEquals("Steady", averageSpeedLabel(30_000)); assertEquals("Slow", averageSpeedLabel(60_000))
  }
}
