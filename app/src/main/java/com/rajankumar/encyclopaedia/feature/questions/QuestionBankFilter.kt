package com.rajankumar.encyclopaedia.feature.questions

import com.rajankumar.encyclopaedia.data.local.QuestionEntity
import com.rajankumar.encyclopaedia.data.local.QuestionTopicEntity

fun List<QuestionEntity>.applyQuestionBankFilters(
  state: QuestionBankFilterState,
  questionTopics: List<QuestionTopicEntity> = emptyList(),
): List<QuestionEntity> {
  val topicQuestionIds = state.topicId?.let(questionTopics::questionIdsForTopic)

  return searchQuestions(state.query)
    .filterQuestions(state.source, state.difficulty)
    .filter { question -> topicQuestionIds == null || question.id in topicQuestionIds }
}

fun availableQuestionSources(questions: List<QuestionEntity>): List<String> =
  (questionSourceOptions + questions.map { it.source.trim().uppercase() })
    .filter(String::isNotBlank)
    .distinct()
