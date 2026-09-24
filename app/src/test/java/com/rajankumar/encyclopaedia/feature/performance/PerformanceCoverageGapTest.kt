package com.rajankumar.encyclopaedia.feature.performance

import org.junit.Assert.assertEquals
import org.junit.Test

class PerformanceCoverageGapTest {
  @Test fun countsUnattemptedQuestions() = assertEquals(7, PerformanceSummary(5, 3, 3, 10, 0).unattemptedQuestions())
  @Test fun inconsistentCountsNeverReturnNegative() = assertEquals(0, PerformanceSummary(5, 3, 12, 10, 0).unattemptedQuestions())
}
