package com.rajankumar.encyclopaedia.feature.performance

import org.junit.Assert.assertEquals
import org.junit.Test

class PerformanceAccuracyLabelTest {
  @Test fun labelsAccuracyBands() {
    assertEquals("Needs attention", accuracyLabel(20)); assertEquals("Developing", accuracyLabel(50)); assertEquals("Good", accuracyLabel(70)); assertEquals("Strong", accuracyLabel(90))
  }
}
