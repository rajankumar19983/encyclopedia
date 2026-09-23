package com.rajankumar.encyclopaedia.feature.performance

import com.rajankumar.encyclopaedia.data.local.KnowledgeNodeEntity
import com.rajankumar.encyclopaedia.data.local.QuestionAttemptEntity
import com.rajankumar.encyclopaedia.data.local.QuestionEntity
import com.rajankumar.encyclopaedia.data.local.QuestionTopicEntity
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class PerformanceDataTest {
  @Test
  fun buildPerformanceDataAggregatesAttemptsAndAverageTime() {
    val questions = listOf(question("q1"), question("q2"), question("q3"))
    val attempts = listOf(
      attempt("a1", "q1", true, 1_000L, 1L),
      attempt("a2", "q1", false, 2_000L, 2L),
      attempt("a3", "q2", true, 3_000L, 3L)
    )

    val data = buildPerformanceData(questions, attempts)

    assertEquals(3, data.summary.attempts)
    assertEquals(2, data.summary.correct)
    assertEquals(2, data.summary.uniqueQuestions)
    assertEquals(3, data.summary.totalQuestions)
    assertEquals(2_000L, data.summary.averageTimeMs)
    assertEquals(66, data.summary.accuracy)
  }

  @Test
  fun negativeAttemptTimesDoNotReduceAverage() {
    val data = buildPerformanceData(
      listOf(question("q1")),
      listOf(attempt("a1", "q1", true, -500L, 1L))
    )

    assertEquals(0L, data.summary.averageTimeMs)
  }

  @Test
  fun performanceDataIncludesLinkedTopicsStillNeedingRevision() {
    val topic = KnowledgeNodeEntity("networks", null, "Computer Networks")
    val attempts = listOf(
      attempt("a1", "q1", false, 2_000L, 1L),
      attempt("a2", "q1", true, 1_500L, 2L),
    )

    val data = buildPerformanceData(
      questions = listOf(question("q1")),
      attempts = attempts,
      topics = listOf(topic),
      questionTopics = listOf(QuestionTopicEntity("q1", "networks")),
    )

    val topicPerformance = data.topicsNeedingRevision.single()
    assertEquals("networks", topicPerformance.topic.id)
    assertEquals(listOf("q1"), topicPerformance.revisionQuestionIds)
    assertEquals(50, topicPerformance.accuracyPercent)
  }

  @Test
  fun recoveredTopicIsNotReportedAsNeedingRevision() {
    val topic = KnowledgeNodeEntity("dbms", null, "DBMS")
    val attempts = listOf(
      attempt("a1", "q1", false, 2_000L, 1L),
      attempt("a2", "q1", true, 1_500L, 2L),
      attempt("a3", "q1", true, 1_000L, 3L),
    )

    val data = buildPerformanceData(
      questions = listOf(question("q1")),
      attempts = attempts,
      topics = listOf(topic),
      questionTopics = listOf(QuestionTopicEntity("q1", "dbms")),
    )

    assertTrue(data.topicsNeedingRevision.isEmpty())
  }

  private fun question(id: String) = QuestionEntity(
    id = id,
    questionText = "Question $id",
    options = "A|B",
    correctAnswer = "A"
  )

  private fun attempt(id: String, questionId: String, correct: Boolean, time: Long, attemptedAt: Long) =
    QuestionAttemptEntity(
      id = id,
      questionId = questionId,
      sessionId = "session",
      selectedAnswer = if (correct) "A" else "B",
      isCorrect = correct,
      timeTakenMs = time,
      attemptedAt = attemptedAt
    )
}
