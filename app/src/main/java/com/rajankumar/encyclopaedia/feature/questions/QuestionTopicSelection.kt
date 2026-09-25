package com.rajankumar.encyclopaedia.feature.questions

import com.rajankumar.encyclopaedia.data.local.QuestionTopicEntity

fun List<QuestionTopicEntity>.questionIdsForTopic(topicId: String): Set<String> =
  asSequence()
    .filter { it.knowledgeNodeId == topicId }
    .map { it.questionId }
    .toSet()

fun List<QuestionTopicEntity>.topicIdForQuestion(questionId: String): String? =
  firstOrNull { it.questionId == questionId }?.knowledgeNodeId
