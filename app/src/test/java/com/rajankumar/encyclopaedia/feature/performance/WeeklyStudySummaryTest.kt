package com.rajankumar.encyclopaedia.feature.performance

import java.time.LocalDate
import org.junit.Assert.assertEquals
import org.junit.Test

class WeeklyStudySummaryTest {
  @Test
  fun `summarises only the rolling seven day window`() {
    val today = LocalDate.of(2026, 9, 23)
    val activity = listOf(
      DailyStudyActivity(today.minusDays(7), 10, 10, 10_000),
      DailyStudyActivity(today.minusDays(6), 4, 3, 8_000),
      DailyStudyActivity(today, 6, 3, 12_000),
    )

    val summary = activity.weeklySummary(today)

    assertEquals(2, summary.activeDays)
    assertEquals(10, summary.attempts)
    assertEquals(6, summary.correct)
    assertEquals(60, summary.accuracyPercent)
    assertEquals(20_000L, summary.totalTimeMs)
  }
}
