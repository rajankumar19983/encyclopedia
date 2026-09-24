package com.rajankumar.encyclopaedia.feature.home

import org.junit.Assert.assertEquals
import org.junit.Test

class HomeProgressSummaryTest {
  @Test fun computesPercent() = assertEquals(60, HomeProgressSummary(6, 10).percent)
  @Test fun emptyTotalIsZero() = assertEquals(0, HomeProgressSummary(2, 0).percent)
}
