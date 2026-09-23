package com.rajankumar.encyclopaedia.feature.performance

import com.rajankumar.encyclopaedia.data.local.QuestionAttemptEntity
import java.time.LocalDate
import java.time.ZoneOffset
import org.junit.Assert.assertEquals
import org.junit.Test

class StudyConsistencyTest {
  private fun attempt(id: String, day: LocalDate) = QuestionAttemptEntity(id, "q-$id", "s", "A", true, 1000, day.atStartOfDay().toInstant(ZoneOffset.UTC).toEpochMilli())

  @Test fun calculatesCurrentAndLongestStreak() {
    val today = LocalDate.of(2026, 9, 22)
    val attempts = listOf(attempt("1", today.minusDays(4)), attempt("2", today.minusDays(3)), attempt("3", today.minusDays(1)), attempt("4", today))
    assertEquals(StudyConsistency(4, 2, 2), attempts.studyConsistency(today, ZoneOffset.UTC))
  }

  @Test fun allowsCurrentStreakToEndYesterday() {
    val today = LocalDate.of(2026, 9, 22)
    val attempts = listOf(attempt("1", today.minusDays(2)), attempt("2", today.minusDays(1)))
    assertEquals(2, attempts.studyConsistency(today, ZoneOffset.UTC).currentStreak)
  }
}
