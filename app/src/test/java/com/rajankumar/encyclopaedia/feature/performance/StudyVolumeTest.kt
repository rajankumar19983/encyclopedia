package com.rajankumar.encyclopaedia.feature.performance

import com.rajankumar.encyclopaedia.data.local.QuestionAttemptEntity
import org.junit.Assert.assertEquals
import org.junit.Test

class StudyVolumeTest {
  @Test fun summarizesAttemptsAndTime() {
    val attempts = listOf(
      QuestionAttemptEntity("1", "q1", "s", "A", true, 1200),
      QuestionAttemptEntity("2", "q2", "s", "B", false, 800),
    )
    assertEquals(StudyVolume(2, 1, 1, 2000), attempts.studyVolume())
    assertEquals(50, attempts.studyVolume().accuracyPercent)
  }
}
