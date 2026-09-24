package com.rajankumar.encyclopaedia.feature.performance

import org.junit.Assert.assertEquals
import org.junit.Test

class PerformancePracticeBalanceTest {
  @Test fun separatesFirstAndRepeatedAttempts() {
    val balance = PerformanceSummary(10, 6, 4, 20, 0).practiceBalance()
    assertEquals(4, balance.firstAttempts); assertEquals(6, balance.repeatedAttempts)
  }
}
