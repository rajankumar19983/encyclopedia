package com.rajankumar.encyclopaedia.feature.accessibility

import com.rajankumar.encyclopaedia.data.local.QuestionEntity
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class QuestionSpeechTest {
  @Test
  fun questionSpeechIncludesAllOptionsWithLetters() {
    val question = QuestionEntity(
      id = "q1",
      questionText = "Which scheduling algorithm is preemptive?",
      options = "Round Robin\nFCFS\nSJF",
      correctAnswer = "A"
    )

    assertEquals(
      "Question. Which scheduling algorithm is preemptive?. Option A. Round Robin. Option B. FCFS. Option C. SJF",
      question.asSpeakableQuestion(listOf("Round Robin", "FCFS", "SJF")).asSpeechText()
    )
  }

  @Test
  fun explanationSpeechUsesExplanationWhenPresent() {
    val question = QuestionEntity(
      id = "q1",
      questionText = "Question",
      options = "One\nTwo",
      correctAnswer = "A",
      explanation = "Round Robin uses a time quantum."
    )

    assertEquals(
      "Explanation. Round Robin uses a time quantum.",
      question.asSpeakableExplanation()?.asSpeechText()
    )
  }

  @Test
  fun blankExplanationProducesNoSpeechContent() {
    val question = QuestionEntity(
      id = "q1",
      questionText = "Question",
      options = "One\nTwo",
      correctAnswer = "A",
      explanation = "   "
    )

    assertNull(question.asSpeakableExplanation())
  }
}
