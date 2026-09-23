package com.rajankumar.encyclopaedia.feature.performance

import org.junit.Assert.assertEquals
import org.junit.Test

class StudyWeekFeedbackTest {
  @Test fun `recognises strong study rhythm`() {
    assertEquals("Strong study rhythm", WeeklyStudySummary(5, 20, 16, 100_000).feedback().headline)
  }

  @Test fun `encourages an empty week`() {
    assertEquals("Start this week's practice", WeeklyStudySummary(0, 0, 0, 0).feedback().headline)
  }
}
