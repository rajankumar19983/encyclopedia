package com.rajankumar.encyclopaedia.feature.performance

import com.rajankumar.encyclopaedia.data.local.KnowledgeNodeEntity
import com.rajankumar.encyclopaedia.data.local.QuestionAttemptEntity
import com.rajankumar.encyclopaedia.data.local.QuestionTopicEntity
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class TopicPerformanceTest {
  private fun attempt(id:String,q:String,correct:Boolean,time:Long)=QuestionAttemptEntity(id,q,"s",if(correct)"A" else "B",correct,1000,time)

  @Test fun includesStrongAndWeakAttemptedTopics() {
    val topics=listOf(KnowledgeNodeEntity("a",null,"A"),KnowledgeNodeEntity("b",null,"B"))
    val links=listOf(QuestionTopicEntity("q1","a"),QuestionTopicEntity("q2","b"))
    val result=buildTopicPerformance(topics,links,listOf(attempt("1","q1",true,1),attempt("2","q2",false,2)))
    assertEquals(2,result.size)
    assertEquals(0,result.first{it.topic.id=="b"}.accuracyPercent)
    assertEquals(100,result.first{it.topic.id=="a"}.accuracyPercent)
  }

  @Test fun multiTopicQuestionContributesToEveryLinkedTopic() {
    val topics=listOf(KnowledgeNodeEntity("a",null,"A"),KnowledgeNodeEntity("b",null,"B"))
    val links=listOf(QuestionTopicEntity("q1","a"),QuestionTopicEntity("q1","b"))
    val result=buildTopicPerformance(topics,links,listOf(attempt("1","q1",false,1)))
    assertTrue(result.all{it.attempts==1&&it.mistakes==1})
  }

  @Test fun ignoresUnattemptedAndMissingTopics() {
    val topics=listOf(KnowledgeNodeEntity("a",null,"A"))
    val links=listOf(QuestionTopicEntity("q1","a"),QuestionTopicEntity("q2","missing"))
    assertTrue(buildTopicPerformance(topics,links,emptyList()).isEmpty())
  }
}
