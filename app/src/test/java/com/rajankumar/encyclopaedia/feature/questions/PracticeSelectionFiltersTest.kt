package com.rajankumar.encyclopaedia.feature.questions

import com.rajankumar.encyclopaedia.data.local.QuestionAttemptEntity
import com.rajankumar.encyclopaedia.data.local.QuestionEntity
import com.rajankumar.encyclopaedia.data.local.QuestionTopicEntity
import org.junit.Assert.assertEquals
import org.junit.Test

class PracticeSelectionFiltersTest {
  private fun question(
    id: String,
    text: String = id,
    source: String = "USER",
    difficulty: String = "MEDIUM",
    options: String = "One\nTwo\nThree\nFour",
    answer: String = "A",
  ) = QuestionEntity(
    id = id,
    questionText = text,
    options = options,
    correctAnswer = answer,
    explanation = "Explanation",
    source = source,
    difficulty = difficulty,
  )

  private fun attempt(id: String, questionId: String, correct: Boolean) = QuestionAttemptEntity(
    id = id,
    questionId = questionId,
    sessionId = "session",
    selectedAnswer = if (correct) "A" else "B",
    isCorrect = correct,
    timeTakenMs = 1000,
  )

  @Test
  fun modeSelectionMatchesAttemptHistorySemantics() {
    val questions = listOf(question("new"), question("wrong"), question("corrected"))
    val attempts = listOf(
      attempt("a1", "wrong", false),
      attempt("a2", "corrected", false),
      attempt("a3", "corrected", true),
    )

    assertEquals(listOf("new"), questions.forPracticeMode(PracticeMode.NEW, attempts).map { it.id })
    assertEquals(
      listOf("wrong", "corrected"),
      questions.forPracticeMode(PracticeMode.MISTAKES, attempts).map { it.id },
    )
  }

  @Test
  fun filtersApplySearchSourceDifficultyTopicAndPracticeValidityTogether() {
    val questions = listOf(
      question("match", "CPU cache coherence", "AI", "HARD"),
      question("wrong-source", "CPU cache coherence", "USER", "HARD"),
      question("wrong-topic", "CPU cache coherence", "AI", "HARD"),
      question("invalid", "CPU cache invalid", "AI", "HARD", options = "Only one", answer = "A"),
    )
    val links = listOf(
      QuestionTopicEntity("match", "cpu"),
      QuestionTopicEntity("wrong-source", "cpu"),
      QuestionTopicEntity("wrong-topic", "memory"),
      QuestionTopicEntity("invalid", "cpu"),
    )
    val config = PracticeSessionConfig(
      query = "cache",
      source = "AI",
      difficulty = "HARD",
      topicId = "cpu",
    )

    val result = filterPracticeCandidates(questions, links, config)
    assertEquals(listOf("match"), result.map { it.id })
  }
}
