package com.rajankumar.encyclopaedia.feature.performance

import org.junit.Assert.assertTrue
import org.junit.Test

class PerformanceCoverageMessageTest {
  @Test fun incompleteCoverageReportsRemaining() = assertTrue(PerformanceCoverage(8, 10).message().contains("2 questions"))
  @Test fun completeCoverageIsExplicit() = assertTrue(PerformanceCoverage(10, 10).message().contains("Every stored question"))
}
