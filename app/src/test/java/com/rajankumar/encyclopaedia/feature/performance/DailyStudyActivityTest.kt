package com.rajankumar.encyclopaedia.feature.performance

import com.rajankumar.encyclopaedia.data.local.QuestionAttemptEntity
import java.time.LocalDate
import java.time.ZoneOffset
import org.junit.Assert.assertEquals
import org.junit.Test

class DailyStudyActivityTest {
  @Test
  fun `groups attempts by local study day`() {
    val day = LocalDate.of(2026, 9, 23)
    val start = day.atStartOfDay(ZoneOffset.UTC).toInstant().toEpochMilli()
    val attempts = listOf(
      attempt("1", start + 1_000, true, 2_000),
      attempt("2", start + 2_000, false, 3_000),
      attempt("3", start + 86_400_000 + 1_000, true, 4_000),
    )

    val activity = attempts.dailyStudyActivity(ZoneOffset.UTC)

    assertEquals(2, activity.size)
    assertEquals(2, activity[0].attempts)
    assertEquals(1, activity[0].correct)
    assertEquals(5_000L, activity[0].totalTimeMs)
    assertEquals(50, activity[0].accuracyPercent)
    assertEquals(day.plusDays(1), activity[1].date)
  }

  private fun attempt(id: String, at: Long, correct: Boolean, duration: Long) = QuestionAttemptEntity(
    id = id,
    questionId = "q-$id",
    sessionId = "session",
    selectedAnswer = "A",
    isCorrect = correct,
    timeTakenMs = duration,
    attemptedAt = at,
  )
}
