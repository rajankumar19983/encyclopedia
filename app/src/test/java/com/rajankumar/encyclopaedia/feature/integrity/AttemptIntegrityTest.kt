package com.rajankumar.encyclopaedia.feature.integrity

import com.rajankumar.encyclopaedia.data.local.QuestionAttemptEntity
import org.junit.Assert.*
import org.junit.Test

class AttemptIntegrityTest {
  private fun attempt(time: Long = 10L, selected: String = "A") = QuestionAttemptEntity("a", "q", "s", selected, true, time, 1L)
  @Test fun acceptsValidAttempt() = assertTrue(attempt().hasValidAttemptFields())
  @Test fun rejectsNegativeTime() = assertFalse(attempt(-1).hasValidAttemptFields())
  @Test fun rejectsBlankSelection() = assertFalse(attempt(selected = "").hasValidAttemptFields())
}
