package com.rajankumar.encyclopaedia.feature.revision

import com.rajankumar.encyclopaedia.data.local.KnowledgeNodeEntity
import com.rajankumar.encyclopaedia.data.local.QuestionAttemptEntity
import com.rajankumar.encyclopaedia.data.local.QuestionTopicEntity

data class TopicRevisionState(
  val topic: KnowledgeNodeEntity,
  val attempts: Int,
  val correctAttempts: Int,
  val mistakes: Int,
  val questionsNeedingRevision: Int,
  val accuracyPercent: Int,
  val priority: RevisionPriority,
)

fun buildTopicRevisionStates(
  topics: List<KnowledgeNodeEntity>,
  questionTopics: List<QuestionTopicEntity>,
  attempts: List<QuestionAttemptEntity>,
): List<TopicRevisionState> {
  val topicById = topics.associateBy { it.id }
  val topicIdsByQuestion = questionTopics.groupBy { it.questionId }
    .mapValues { (_, links) -> links.map { it.knowledgeNodeId }.distinct() }
  val histories = attempts.groupBy { it.questionId }

  val revisionQuestionIds = histories.mapNotNull { (questionId, history) ->
    if (history.needsRevision()) questionId else null
  }.toSet()

  return questionTopics.asSequence()
    .map { it.knowledgeNodeId }
    .distinct()
    .mapNotNull { topicId ->
      val topic = topicById[topicId] ?: return@mapNotNull null
      val questionIds = topicIdsByQuestion.filterValues { topicId in it }.keys
      val topicAttempts = questionIds.flatMap { histories[it].orEmpty() }
      if (topicAttempts.none { !it.isCorrect }) return@mapNotNull null

      val mistakes = topicAttempts.count { !it.isCorrect }
      val correct = topicAttempts.count { it.isCorrect }
      val needingRevision = questionIds.count { it in revisionQuestionIds }
      if (needingRevision == 0) return@mapNotNull null

      TopicRevisionState(
        topic = topic,
        attempts = topicAttempts.size,
        correctAttempts = correct,
        mistakes = mistakes,
        questionsNeedingRevision = needingRevision,
        accuracyPercent = (correct * 100) / topicAttempts.size,
        priority = revisionPriority(mistakes, topicAttempts.size),
      )
    }
    .sortedWith(
      compareBy<TopicRevisionState> { it.priority.ordinal }
        .thenByDescending { it.mistakes }
        .thenBy { it.topic.name.lowercase() }
    )
    .toList()
}

internal fun List<QuestionAttemptEntity>.needsRevision(): Boolean {
  if (isEmpty()) return false
  val history = sortedBy { it.attemptedAt }
  val latestIncorrectIndex = history.indexOfLast { !it.isCorrect }
  if (latestIncorrectIndex < 0) return false
  val correctSinceLatestMistake = history.drop(latestIncorrectIndex + 1).count { it.isCorrect }
  return correctSinceLatestMistake < 2
}
