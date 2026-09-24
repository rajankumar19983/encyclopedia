package com.rajankumar.encyclopaedia.feature.performance

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class PerformanceCompletionTest {
  @Test fun completeBankIsDetected() = assertTrue(PerformanceSummary(10, 8, 10, 10, 0).hasAttemptedAllQuestions())
  @Test fun emptyBankIsNotComplete() = assertFalse(PerformanceSummary(0, 0, 0, 0, 0).hasAttemptedAllQuestions())
}
