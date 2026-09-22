package com.rajankumar.encyclopaedia.feature.performance

import com.rajankumar.encyclopaedia.data.local.KnowledgeNodeEntity
import org.junit.Assert.assertEquals
import org.junit.Test

class TopicPerformanceSummaryTest {
  private fun topic(id:String,attempts:Int,accuracy:Int)=TopicPerformance(
    topic=KnowledgeNodeEntity(id,null,id),
    attempts=attempts,
    correct=attempts*accuracy/100,
    mistakes=attempts-(attempts*accuracy/100),
    accuracyPercent=accuracy,
    practisedQuestions=1,
  )

  @Test fun emptySummaryIsZeroed() {
    assertEquals(TopicPerformanceSummary(0,0,0,0),emptyList<TopicPerformance>().summary())
  }

  @Test fun separatesStrongFromDevelopingTopics() {
    val summary=listOf(topic("strong",4,100),topic("weak",4,50),topic("new",1,100)).summary()
    assertEquals(3,summary.attemptedTopics)
    assertEquals(1,summary.strongTopics)
    assertEquals(2,summary.developingTopics)
    assertEquals(83,summary.averageAccuracy)
  }
}
