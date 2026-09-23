package com.rajankumar.encyclopaedia.feature.performance

import com.rajankumar.encyclopaedia.data.local.KnowledgeNodeEntity
import org.junit.Assert.assertEquals
import org.junit.Test

class TopicPerformancePriorityTest {
  private fun item(id: String, accuracy: Int, mistakes: Int) = TopicPerformance(
    KnowledgeNodeEntity(id, id, "TOPIC"), 10, 10 - mistakes, mistakes, accuracy, 3
  )

  @Test fun weakestRepeatedMistakeTopicRanksFirst() {
    val result = listOf(item("strong", 90, 1), item("weak", 40, 6), item("medium", 65, 3)).studyPriorities()
    assertEquals("weak", result.first().performance.topic.id)
    assertEquals("Low accuracy", result.first().reason)
  }

  @Test fun respectsLimit() {
    val result = listOf(item("a", 40, 4), item("b", 50, 3), item("c", 60, 2)).studyPriorities(2)
    assertEquals(2, result.size)
  }
}
