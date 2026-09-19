package com.rajankumar.encyclopaedia.feature.revision

import com.rajankumar.encyclopaedia.data.local.QuestionAttemptEntity
import com.rajankumar.encyclopaedia.data.local.QuestionEntity
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class RevisionBuilderTest {
  private val question = QuestionEntity(
    id = "q1",
    questionText = "Question",
    options = "A|B",
    correctAnswer = "A"
  )

  @Test
  fun incorrectAttemptAddsQuestionToRevisionQueue() {
    val queue = buildRevisionQueue(listOf(question), listOf(attempt("a1", false, 1L)))

    assertEquals(listOf("q1"), queue.map { it.question.id })
  }

  @Test
  fun twoCorrectAttemptsAfterLatestMistakeRemoveQuestionFromQueue() {
    val attempts = listOf(
      attempt("a1", false, 1L),
      attempt("a2", true, 2L),
      attempt("a3", true, 3L)
    )

    assertTrue(buildRevisionQueue(listOf(question), attempts).isEmpty())
  }

  @Test
  fun newMistakeAfterCorrectAnswersReturnsQuestionToQueue() {
    val attempts = listOf(
      attempt("a1", false, 1L),
      attempt("a2", true, 2L),
      attempt("a3", true, 3L),
      attempt("a4", false, 4L)
    )

    assertEquals(listOf("q1"), buildRevisionQueue(listOf(question), attempts).map { it.question.id })
  }

  private fun attempt(id: String, correct: Boolean, attemptedAt: Long) = QuestionAttemptEntity(
    id = id,
    questionId = question.id,
    sessionId = "session",
    selectedAnswer = if (correct) "A" else "B",
    isCorrect = correct,
    timeTakenMs = 1_000L,
    attemptedAt = attemptedAt
  )
}
