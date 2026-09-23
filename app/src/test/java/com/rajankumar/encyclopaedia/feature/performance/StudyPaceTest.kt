package com.rajankumar.encyclopaedia.feature.performance

import org.junit.Assert.assertEquals
import org.junit.Test

class StudyPaceTest {
  @Test fun `calculates pace across active days`() {
    val pace = WeeklyStudySummary(4, 28, 20, 120_000).studyPace()
    assertEquals(7, pace.attemptsPerActiveDay)
    assertEquals(30_000L, pace.averageTimePerActiveDayMs)
  }

  @Test fun `returns zero pace without active days`() {
    assertEquals(StudyPace(0, 0), WeeklyStudySummary(0, 0, 0, 0).studyPace())
  }
}
