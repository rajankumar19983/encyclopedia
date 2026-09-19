package com.rajankumar.encyclopaedia.feature.revision

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class RevisionPracticeRequestTest {
  @Before
  fun clearRequest() {
    RevisionPracticeRequest.consume()
  }

  @Test
  fun requestRemovesDuplicateQuestionIds() {
    RevisionPracticeRequest.set(listOf("q1", "q2", "q1"))

    assertEquals(listOf("q1", "q2"), RevisionPracticeRequest.consume())
  }

  @Test
  fun consumeClearsPendingRequest() {
    RevisionPracticeRequest.set(listOf("q1"))

    assertEquals(listOf("q1"), RevisionPracticeRequest.consume())
    assertTrue(RevisionPracticeRequest.consume().isEmpty())
  }
}
