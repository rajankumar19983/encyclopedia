package com.rajankumar.encyclopaedia.feature.performance

import com.rajankumar.encyclopaedia.data.local.KnowledgeNodeEntity
import org.junit.Assert.assertTrue
import org.junit.Test

class TopicPerformanceInsightTest {
  @Test fun pointsAtWeakestTopic() {
    val item = TopicPerformance(KnowledgeNodeEntity("os", "Operating Systems", "TOPIC"), 10, 4, 6, 40, 3)
    val insight = topicPerformanceInsight(performanceCoverage(3, listOf(item)), listOf(TopicPerformancePriority(item, 78, "Low accuracy")))
    assertTrue(insight.headline.contains("Operating Systems"))
  }

  @Test fun encouragesCoverageWhenNoWeakTopicDominates() {
    val coverage = TopicPerformanceCoverage(4, 2, 2, 0)
    assertTrue(topicPerformanceInsight(coverage, emptyList()).headline.contains("coverage", ignoreCase = true))
  }
}
