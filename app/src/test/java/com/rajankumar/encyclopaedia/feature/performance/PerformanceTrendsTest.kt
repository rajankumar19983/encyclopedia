package com.rajankumar.encyclopaedia.feature.performance

import com.rajankumar.encyclopaedia.data.local.QuestionAttemptEntity
import org.junit.Assert.assertEquals
import org.junit.Test

class PerformanceTrendsTest {
  @Test
  fun accuracyTrendComparesNewestWindowWithPreviousWindow() {
    val attempts = listOf(
      attempt("a1", false, 1L),
      attempt("a2", false, 2L),
      attempt("a3", true, 3L),
      attempt("a4", true, 4L)
    )

    val trend = attempts.accuracyTrend(window = 2)

    assertEquals(100, trend.recent)
    assertEquals(0, trend.previous)
    assertEquals(100, trend.change)
  }

  @Test
  fun chunkedPerformanceKeepsChronologicalBuckets() {
    val attempts = listOf(
      attempt("a3", true, 3L),
      attempt("a1", true, 1L),
      attempt("a2", false, 2L)
    )

    val buckets = attempts.chunkedPerformance(size = 2)

    assertEquals(2, buckets.size)
    assertEquals("1–2", buckets[0].label)
    assertEquals(50, buckets[0].accuracy)
    assertEquals("3–3", buckets[1].label)
    assertEquals(100, buckets[1].accuracy)
  }

  private fun attempt(id: String, correct: Boolean, attemptedAt: Long) = QuestionAttemptEntity(
    id = id,
    questionId = "q1",
    sessionId = "session",
    selectedAnswer = if (correct) "A" else "B",
    isCorrect = correct,
    timeTakenMs = 1_000L,
    attemptedAt = attemptedAt
  )
}
