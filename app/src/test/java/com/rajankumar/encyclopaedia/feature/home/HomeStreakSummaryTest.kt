package com.rajankumar.encyclopaedia.feature.home

import org.junit.Assert.assertEquals
import org.junit.Test

class HomeStreakSummaryTest {
  @Test fun singularDayReadsNaturally() = assertEquals("1 day", HomeStreakSummary(1).label)
  @Test fun negativeStreakIsBounded() = assertEquals("0 days", HomeStreakSummary(-2).label)
}
