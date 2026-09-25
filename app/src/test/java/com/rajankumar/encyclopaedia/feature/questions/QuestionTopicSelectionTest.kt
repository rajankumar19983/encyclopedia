package com.rajankumar.encyclopaedia.feature.questions

import com.rajankumar.encyclopaedia.data.local.QuestionTopicEntity
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class QuestionTopicSelectionTest {
  private val links = listOf(
    QuestionTopicEntity("q1", "cpu"),
    QuestionTopicEntity("q2", "cpu"),
    QuestionTopicEntity("q3", "memory"),
  )

  @Test
  fun resolvesExistingTopicForEditor() {
    assertEquals("cpu", links.topicIdForQuestion("q1"))
    assertNull(links.topicIdForQuestion("missing"))
  }

  @Test
  fun topicFilterCollectsOnlyLinkedQuestionIds() {
    assertEquals(setOf("q1", "q2"), links.questionIdsForTopic("cpu"))
    assertEquals(emptySet<String>(), links.questionIdsForTopic("unknown"))
  }
}
