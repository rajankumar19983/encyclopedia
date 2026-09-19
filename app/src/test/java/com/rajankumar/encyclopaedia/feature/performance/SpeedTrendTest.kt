package com.rajankumar.encyclopaedia.feature.performance

import com.rajankumar.encyclopaedia.data.local.QuestionAttemptEntity
import org.junit.Assert.assertEquals
import org.junit.Test

class SpeedTrendTest {
  @Test
  fun speedTrendComparesRecentAndPreviousWindows() {
    val attempts = listOf(
      attempt("a1", 4_000L, 1L),
      attempt("a2", 2_000L, 2L),
      attempt("a3", 1_000L, 3L),
      attempt("a4", 1_000L, 4L)
    )

    val trend = attempts.speedTrend(window = 2)

    assertEquals(1_000L, trend.recentAverageMs)
    assertEquals(3_000L, trend.previousAverageMs)
    assertEquals(2_000L, trend.improvementMs)
  }

  @Test
  fun negativeTimesAreTreatedAsZero() {
    val trend = listOf(attempt("a1", -1L, 1L)).speedTrend(window = 1)

    assertEquals(0L, trend.recentAverageMs)
  }

  private fun attempt(id: String, time: Long, attemptedAt: Long) = QuestionAttemptEntity(
    id = id,
    questionId = "q1",
    sessionId = "session",
    selectedAnswer = "A",
    isCorrect = true,
    timeTakenMs = time,
    attemptedAt = attemptedAt
  )
}
