package com.rajankumar.encyclopaedia.feature.teacher

import com.rajankumar.encyclopaedia.data.local.QuestionEntity
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class QuestionTeacherContextTest {
  private val question = QuestionEntity(
    id = "q1",
    questionText = "Which scheduling algorithm is preemptive?",
    options = "Round Robin\nFCFS\nSJF",
    correctAnswer = "A",
    explanation = "Round Robin allocates a time quantum to each process."
  )

  @Test
  fun unansweredQuestionDoesNotLeakAnswerOrExplanation() {
    val context = question.asTeacherContext(
      options = listOf("Round Robin", "FCFS", "SJF"),
      answerRevealed = false
    ).asPromptContext()

    assertTrue(context.contains("A. Round Robin"))
    assertTrue(context.contains("B. FCFS"))
    assertFalse(context.contains("Correct answer:"))
    assertFalse(context.contains("time quantum"))
  }

  @Test
  fun revealedQuestionIncludesAnswerAndExplanation() {
    val context = question.asTeacherContext(
      options = listOf("Round Robin", "FCFS", "SJF"),
      answerRevealed = true
    ).asPromptContext()

    assertTrue(context.contains("Correct answer: A"))
    assertTrue(context.contains("Round Robin allocates a time quantum"))
  }
}
