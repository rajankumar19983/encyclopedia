package com.rajankumar.encyclopaedia.feature.home

import org.junit.Assert.assertEquals
import org.junit.Test

class HomeTodaySummaryTest {
  @Test fun combinesPlannerAndRevisionStatus() = assertEquals("2 of 4 tasks complete • 3 questions to revise", HomeTodaySummary(2, 4, 3).label())
}
