package com.rajankumar.encyclopaedia.feature.performance

import org.junit.Assert.assertEquals
import org.junit.Test

class PerformanceErrorCountTest {
  @Test fun derivesMistakesFromSummary() = assertEquals(4, PerformanceSummary(10, 6, 5, 10, 0).mistakes())
  @Test fun inconsistentSummaryCannotProduceNegativeMistakes() = assertEquals(0, PerformanceSummary(3, 5, 3, 3, 0).mistakes())
}
