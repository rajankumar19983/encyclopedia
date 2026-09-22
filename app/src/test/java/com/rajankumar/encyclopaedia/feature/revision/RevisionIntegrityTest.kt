package com.rajankumar.encyclopaedia.feature.revision

import com.rajankumar.encyclopaedia.data.local.KnowledgeNodeEntity
import com.rajankumar.encyclopaedia.data.local.QuestionAttemptEntity
import com.rajankumar.encyclopaedia.data.local.QuestionEntity
import com.rajankumar.encyclopaedia.data.local.QuestionTopicEntity
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class RevisionIntegrityTest {
  private val q1=QuestionEntity(id="q1",questionText="Q1",options="A\nB",correctAnswer="A")
  private val q2=QuestionEntity(id="q2",questionText="Q2",options="A\nB",correctAnswer="A")
  private fun wrong(id:String,q:String)=QuestionAttemptEntity(id=id,questionId=q,sessionId="s",selectedAnswer="B",isCorrect=false,timeTakenMs=1,attemptedAt=1)

  @Test fun taggedDueQuestionsMatchQuestionQueue() {
    val attempts=listOf(wrong("a1","q1"))
    val queue=buildRevisionQueue(listOf(q1),attempts)
    val topics=buildTopicRevisionStates(listOf(KnowledgeNodeEntity("t",null,"Topic")),listOf(QuestionTopicEntity("q1","t")),attempts)
    val summary=revisionIntegritySummary(queue,topics)
    assertTrue(summary.taggedDueQuestionsAreConsistent)
    assertEquals(setOf("q1"),summary.topicDueQuestionIds)
  }

  @Test fun untaggedDueQuestionsRemainInQuestionRevision() {
    val attempts=listOf(wrong("a1","q1"),wrong("a2","q2"))
    val queue=buildRevisionQueue(listOf(q1,q2),attempts)
    val topics=buildTopicRevisionStates(listOf(KnowledgeNodeEntity("t",null,"Topic")),listOf(QuestionTopicEntity("q1","t")),attempts)
    assertEquals(setOf("q2"),revisionIntegritySummary(queue,topics).untaggedDueQuestionIds)
  }
}
