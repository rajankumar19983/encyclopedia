package com.rajankumar.encyclopaedia.feature.questions

import com.rajankumar.encyclopaedia.data.local.EncyclopaediaDao
import com.rajankumar.encyclopaedia.data.local.QuestionEntity

suspend fun EncyclopaediaDao.loadPracticeQuestions(mode: PracticeMode, limit: Int): List<QuestionEntity> {
  val requested = limit.coerceIn(1, PracticeConstants.maxSessionQuestions)
  val candidates = when (mode) {
    PracticeMode.RANDOM -> getRandomQuestions(PracticeConstants.maxSessionQuestions)
    PracticeMode.NEW -> getUnattemptedQuestions(PracticeConstants.maxSessionQuestions)
    PracticeMode.MISTAKES -> getPreviouslyIncorrectQuestions(PracticeConstants.maxSessionQuestions)
  }
  return candidates.filter { it.validateForPractice().valid }.take(requested)
}
