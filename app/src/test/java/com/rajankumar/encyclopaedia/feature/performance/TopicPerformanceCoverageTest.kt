package com.rajankumar.encyclopaedia.feature.performance

import com.rajankumar.encyclopaedia.data.local.KnowledgeNodeEntity
import org.junit.Assert.assertEquals
import org.junit.Test

class TopicPerformanceCoverageTest {
  private fun performance(id: String, accuracy: Int) = TopicPerformance(
    KnowledgeNodeEntity(id, "Topic $id", "TOPIC"), 10, accuracy / 10, 10 - accuracy / 10, accuracy, 2
  )

  @Test fun summarizesPractisedStrongWeakAndUnpractisedTopics() {
    val result = performanceCoverage(5, listOf(performance("a", 90), performance("b", 50), performance("c", 70)))
    assertEquals(3, result.practisedTopicCount)
    assertEquals(2, result.unpractisedTopicCount)
    assertEquals(1, result.strongTopicCount)
    assertEquals(1, result.weakTopicCount)
    assertEquals(60, result.coveragePercent)
  }

  @Test fun emptyCurriculumHasZeroCoverage() = assertEquals(0, performanceCoverage(0, emptyList()).coveragePercent)
}
