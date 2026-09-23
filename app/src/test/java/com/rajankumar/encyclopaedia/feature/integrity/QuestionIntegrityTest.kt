package com.rajankumar.encyclopaedia.feature.integrity

import com.rajankumar.encyclopaedia.data.local.QuestionEntity
import org.junit.Assert.*
import org.junit.Test

class QuestionIntegrityTest {
  private fun question(options: String = "A\nB", answer: String = "A") = QuestionEntity("q", "Question?", options, answer)
  @Test fun acceptsCompleteQuestion() = assertTrue(question().hasValidQuestionFields())
  @Test fun rejectsSingleOptionQuestion() = assertFalse(question("A").hasValidQuestionFields())
  @Test fun rejectsMissingAnswer() = assertFalse(question(answer = "").hasValidQuestionFields())
}
