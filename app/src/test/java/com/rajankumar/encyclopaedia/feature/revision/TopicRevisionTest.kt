package com.rajankumar.encyclopaedia.feature.revision

import com.rajankumar.encyclopaedia.data.local.KnowledgeNodeEntity
import com.rajankumar.encyclopaedia.data.local.QuestionAttemptEntity
import com.rajankumar.encyclopaedia.data.local.QuestionTopicEntity
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class TopicRevisionTest {
  private val operatingSystems = KnowledgeNodeEntity("os", null, "Operating Systems")
  private val scheduling = KnowledgeNodeEntity("sched", "os", "Scheduling")

  private fun attempt(
    id: String,
    questionId: String,
    correct: Boolean,
    time: Long,
  ) = QuestionAttemptEntity(
    id = id,
    questionId = questionId,
    sessionId = "session",
    selectedAnswer = if (correct) "correct" else "wrong",
    isCorrect = correct,
    timeTakenMs = 1000,
    attemptedAt = time,
  )

  @Test
  fun wrongAnswerMarksEveryLinkedTopicForRevision() {
    val states = buildTopicRevisionStates(
      topics = listOf(operatingSystems, scheduling),
      questionTopics = listOf(
        QuestionTopicEntity("q1", "os"),
        QuestionTopicEntity("q1", "sched"),
      ),
      attempts = listOf(attempt("a1", "q1", false, 1)),
    )

    assertEquals(setOf("os", "sched"), states.map { it.topic.id }.toSet())
    assertTrue(states.all { it.questionsNeedingRevision == 1 })
    assertTrue(states.all { it.revisionQuestionIds == listOf("q1") })
    assertTrue(states.all { it.mistakes == 1 })
  }

  @Test
  fun topicRevisionContainsOnlyQuestionsStillDue() {
    val state = buildTopicRevisionStates(
      topics = listOf(scheduling),
      questionTopics = listOf(
        QuestionTopicEntity("q1", "sched"),
        QuestionTopicEntity("q2", "sched"),
        QuestionTopicEntity("q3", "sched"),
      ),
      attempts = listOf(
        attempt("a1", "q1", false, 1),
        attempt("a2", "q2", false, 2),
        attempt("a3", "q2", true, 3),
        attempt("a4", "q2", true, 4),
        attempt("a5", "q3", true, 5),
      ),
    ).single()

    assertEquals(listOf("q1"), state.revisionQuestionIds)
    assertEquals(1, state.questionsNeedingRevision)
  }

  @Test
  fun dueQuestionIdsAreStableAndDeduplicated() {
    val state = buildTopicRevisionStates(
      topics = listOf(scheduling),
      questionTopics = listOf(
        QuestionTopicEntity("q2", "sched"),
        QuestionTopicEntity("q1", "sched"),
        QuestionTopicEntity("q1", "sched"),
      ),
      attempts = listOf(
        attempt("a1", "q2", false, 1),
        attempt("a2", "q1", false, 2),
      ),
    ).single()

    assertEquals(listOf("q1", "q2"), state.revisionQuestionIds)
    assertEquals(2, state.questionsNeedingRevision)
  }

  @Test
  fun twoCorrectAttemptsAfterLatestMistakeClearTopicRevision() {
    val states = buildTopicRevisionStates(
      topics = listOf(scheduling),
      questionTopics = listOf(QuestionTopicEntity("q1", "sched")),
      attempts = listOf(
        attempt("a1", "q1", false, 1),
        attempt("a2", "q1", true, 2),
        attempt("a3", "q1", true, 3),
      ),
    )

    assertTrue(states.isEmpty())
  }

  @Test
  fun oneCorrectAttemptDoesNotPrematurelyClearRevision() {
    val states = buildTopicRevisionStates(
      topics = listOf(scheduling),
      questionTopics = listOf(QuestionTopicEntity("q1", "sched")),
      attempts = listOf(
        attempt("a1", "q1", false, 1),
        attempt("a2", "q1", true, 2),
      ),
    )

    assertEquals(1, states.size)
    assertEquals(1, states.single().questionsNeedingRevision)
    assertEquals(listOf("q1"), states.single().revisionQuestionIds)
    assertEquals(50, states.single().accuracyPercent)
  }

  @Test
  fun laterMistakeResetsRecoveryProgress() {
    val history = listOf(
      attempt("a1", "q1", false, 1),
      attempt("a2", "q1", true, 2),
      attempt("a3", "q1", false, 3),
      attempt("a4", "q1", true, 4),
    )

    assertTrue(history.needsRevision())
  }

  @Test
  fun repeatedMistakesIncreaseTopicMistakeCount() {
    val state = buildTopicRevisionStates(
      topics = listOf(scheduling),
      questionTopics = listOf(QuestionTopicEntity("q1", "sched")),
      attempts = listOf(
        attempt("a1", "q1", false, 1),
        attempt("a2", "q1", false, 2),
        attempt("a3", "q1", false, 3),
      ),
    ).single()

    assertEquals(3, state.mistakes)
    assertEquals(3, state.attempts)
    assertEquals(0, state.accuracyPercent)
  }

  @Test
  fun untaggedWrongQuestionDoesNotCreateFakeTopicState() {
    val states = buildTopicRevisionStates(
      topics = listOf(scheduling),
      questionTopics = emptyList(),
      attempts = listOf(attempt("a1", "q1", false, 1)),
    )

    assertTrue(states.isEmpty())
  }

  @Test
  fun missingTopicRecordIsIgnoredSafely() {
    val states = buildTopicRevisionStates(
      topics = emptyList(),
      questionTopics = listOf(QuestionTopicEntity("q1", "missing")),
      attempts = listOf(attempt("a1", "q1", false, 1)),
    )

    assertTrue(states.isEmpty())
  }
}
