package com.rajankumar.encyclopaedia.feature.performance

import com.rajankumar.encyclopaedia.data.local.KnowledgeNodeEntity
import com.rajankumar.encyclopaedia.data.local.QuestionAttemptEntity
import com.rajankumar.encyclopaedia.data.local.QuestionEntity
import com.rajankumar.encyclopaedia.data.local.QuestionTopicEntity
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class PerformanceTopicIntegrationTest {
  private val question=QuestionEntity(id="q",questionText="Question",options="A\nB",correctAnswer="A")
  private val topic=KnowledgeNodeEntity("t",null,"Topic")
  private val link=QuestionTopicEntity("q","t")
  private fun attempt(id:String,correct:Boolean,time:Long)=QuestionAttemptEntity(id,"q","s",if(correct)"A" else "B",correct,1000,time)

  @Test fun masteredTopicRemainsVisibleInAnalyticsButLeavesRevision() {
    val attempts=listOf(attempt("1",false,1),attempt("2",true,2),attempt("3",true,3))
    val data=buildPerformanceData(listOf(question),attempts,listOf(topic),listOf(link))
    assertEquals(1,data.topicPerformance.size)
    assertTrue(data.topicsNeedingRevision.isEmpty())
  }

  @Test fun weakTopicAppearsInAnalyticsAndRevision() {
    val data=buildPerformanceData(listOf(question),listOf(attempt("1",false,1)),listOf(topic),listOf(link))
    assertEquals(1,data.topicPerformance.size)
    assertEquals(1,data.topicsNeedingRevision.size)
  }
}
