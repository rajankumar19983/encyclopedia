package com.rajankumar.encyclopaedia.feature.performance

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class PerformanceCoverageTest {
  @Test fun computesCoverage() {
    val coverage = PerformanceCoverage(15, 20)
    assertEquals(75, coverage.percent)
    assertEquals(5, coverage.remaining)
  }
  @Test fun overcountIsBounded() = assertTrue(PerformanceCoverage(30, 20).complete)
}
