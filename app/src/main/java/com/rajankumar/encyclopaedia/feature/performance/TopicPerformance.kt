package com.rajankumar.encyclopaedia.feature.performance

import com.rajankumar.encyclopaedia.data.local.KnowledgeNodeEntity
import com.rajankumar.encyclopaedia.data.local.QuestionAttemptEntity
import com.rajankumar.encyclopaedia.data.local.QuestionTopicEntity

data class TopicPerformance(
  val topic: KnowledgeNodeEntity,
  val attempts: Int,
  val correct: Int,
  val mistakes: Int,
  val accuracyPercent: Int,
  val practisedQuestions: Int,
)

fun buildTopicPerformance(
  topics: List<KnowledgeNodeEntity>,
  links: List<QuestionTopicEntity>,
  attempts: List<QuestionAttemptEntity>,
): List<TopicPerformance> {
  val topicById = topics.associateBy { it.id }
  val attemptsByQuestion = attempts.groupBy { it.questionId }
  return links.groupBy { it.knowledgeNodeId }.mapNotNull { (topicId, topicLinks) ->
    val topic = topicById[topicId] ?: return@mapNotNull null
    val questionIds = topicLinks.map { it.questionId }.distinct()
    val topicAttempts = questionIds.flatMap { attemptsByQuestion[it].orEmpty() }
    if (topicAttempts.isEmpty()) return@mapNotNull null
    val correct = topicAttempts.count { it.isCorrect }
    TopicPerformance(
      topic = topic,
      attempts = topicAttempts.size,
      correct = correct,
      mistakes = topicAttempts.size - correct,
      accuracyPercent = correct * 100 / topicAttempts.size,
      practisedQuestions = questionIds.count { attemptsByQuestion[it].orEmpty().isNotEmpty() },
    )
  }.sortedWith(compareBy<TopicPerformance> { it.accuracyPercent }.thenByDescending { it.attempts }.thenBy { it.topic.name.lowercase() })
}
