package com.rajankumar.encyclopaedia.feature.questions

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class AiQuestionValidatorTest {
  private fun validDraft(text: String = "What is RAM?") = AiQuestionDraft(
    questionText = text,
    options = listOf("A", "B", "C", "D"),
    correctIndex = 1,
    explanation = "Because B is correct.",
    difficulty = "MEDIUM",
  )

  @Test fun acceptsValidProposal() = assertTrue(AiQuestionValidator.validate(AiQuestionProposal(listOf(validDraft()))).isValid)

  @Test fun rejectsTooFewOptions() {
    val result = AiQuestionValidator.validate(AiQuestionProposal(listOf(validDraft().copy(options = listOf("A", "B", "C")))))
    assertFalse(result.isValid)
  }

  @Test fun rejectsDuplicateQuestions() {
    val result = AiQuestionValidator.validate(AiQuestionProposal(listOf(validDraft(), validDraft("What is RAM ?"))))
    assertFalse(result.isValid)
  }

  @Test fun rejectsInvalidAnswerIndex() {
    val result = AiQuestionValidator.validate(AiQuestionProposal(listOf(validDraft().copy(correctIndex = 9))))
    assertFalse(result.isValid)
  }
}
