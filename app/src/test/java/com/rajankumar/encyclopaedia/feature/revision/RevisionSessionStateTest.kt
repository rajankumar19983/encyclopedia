package com.rajankumar.encyclopaedia.feature.revision

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class RevisionSessionStateTest {
  @Test
  fun completingQuestionsUpdatesProgressAndNextQuestion() {
    val initial = RevisionSessionState(listOf("q1", "q2"))

    assertEquals("q1", initial.nextQuestionId())
    assertEquals(0, initial.progress.completed)

    val afterFirst = initial.markCompleted("q1")
    assertEquals("q2", afterFirst.nextQuestionId())
    assertEquals(1, afterFirst.progress.completed)
    assertEquals("1 of 2 completed", afterFirst.progress.summary)
    assertFalse(afterFirst.isComplete)

    val complete = afterFirst.markCompleted("q2")
    assertEquals(2, complete.progress.completed)
    assertTrue(complete.isComplete)
    assertEquals(null, complete.nextQuestionId())
  }

  @Test
  fun unknownQuestionDoesNotChangeSession() {
    val state = RevisionSessionState(listOf("q1"))

    assertEquals(state, state.markCompleted("other"))
  }
}
