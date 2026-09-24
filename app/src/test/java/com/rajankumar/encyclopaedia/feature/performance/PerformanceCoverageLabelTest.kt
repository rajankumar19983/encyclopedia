package com.rajankumar.encyclopaedia.feature.performance

import org.junit.Assert.assertEquals
import org.junit.Test

class PerformanceCoverageLabelTest {
  @Test fun labelsCoverageBands() {
    assertEquals("Just started", coverageLabel(10)); assertEquals("Growing", coverageLabel(40)); assertEquals("Broad", coverageLabel(60)); assertEquals("Extensive", coverageLabel(90))
  }
}
