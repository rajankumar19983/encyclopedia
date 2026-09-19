package com.rajankumar.encyclopaedia.feature.performance

import com.rajankumar.encyclopaedia.data.local.QuestionAttemptEntity
import org.junit.Assert.assertEquals
import org.junit.Test

class PerformancePeriodFilterTest {
  private val day = 86_400_000L
  private val now = 100L * day

  @Test
  fun weekIncludesBoundaryAndRecentAttempts() {
    val attempts = listOf(
      attempt("old", now - 8 * day),
      attempt("boundary", now - 7 * day),
      attempt("recent", now - day)
    )

    assertEquals(listOf("boundary", "recent"), attempts.withinPeriod(PerformancePeriod.WEEK, now).map { it.id })
  }

  @Test
  fun allTimeReturnsEveryAttempt() {
    val attempts = listOf(attempt("old", 1L), attempt("recent", now))

    assertEquals(attempts, attempts.withinPeriod(PerformancePeriod.ALL, now))
  }

  private fun attempt(id: String, attemptedAt: Long) = QuestionAttemptEntity(
    id = id,
    questionId = "q1",
    sessionId = "session",
    selectedAnswer = "A",
    isCorrect = true,
    timeTakenMs = 1_000L,
    attemptedAt = attemptedAt
  )
}
