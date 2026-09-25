package com.rajankumar.encyclopaedia.feature.questions

import com.rajankumar.encyclopaedia.data.local.EncyclopaediaDao
import com.rajankumar.encyclopaedia.data.local.QuestionEntity

suspend fun EncyclopaediaDao.loadPracticeQuestions(config: PracticeSessionConfig): List<QuestionEntity> {
  val candidates = when (config.mode) {
    PracticeMode.RANDOM -> getAllQuestionsOnce()
    PracticeMode.NEW -> getAllUnattemptedQuestionsForPractice()
    PracticeMode.MISTAKES -> getAllPreviouslyIncorrectQuestionsForPractice()
  }
  val questionTopics = if (config.topicId.isNullOrBlank()) emptyList() else getAllQuestionTopicsOnce()

  return filterPracticeCandidates(candidates, questionTopics, config)
    .shuffled()
    .take(config.safeQuestionCount)
}

suspend fun EncyclopaediaDao.loadPracticeQuestions(mode: PracticeMode, limit: Int): List<QuestionEntity> =
  loadPracticeQuestions(PracticeSessionConfig(mode = mode, questionCount = limit))

suspend fun EncyclopaediaDao.loadRevisionPracticeQuestions(questionIds: List<String>): List<QuestionEntity> {
  if (questionIds.isEmpty()) return emptyList()
  val byId = getQuestionsByIds(questionIds).associateBy { it.id }
  return questionIds.mapNotNull(byId::get).filter { it.validateForPractice().valid }
}
