package com.rajankumar.encyclopaedia.feature.questions

import com.rajankumar.encyclopaedia.data.local.QuestionAttemptEntity
import com.rajankumar.encyclopaedia.data.local.QuestionEntity
import com.rajankumar.encyclopaedia.data.local.QuestionTopicEntity

fun selectPracticeQuestions(source: List<QuestionEntity>, limit: Int): List<QuestionEntity> = source
  .asSequence()
  .filter { it.validateForPractice().valid }
  .shuffled()
  .take(limit.coerceAtLeast(1))
  .toList()

internal fun List<QuestionEntity>.forPracticeMode(
  mode: PracticeMode,
  attempts: List<QuestionAttemptEntity>,
): List<QuestionEntity> {
  if (mode == PracticeMode.RANDOM) return this

  val attemptedIds = attempts.asSequence().map { it.questionId }.toSet()
  return when (mode) {
    PracticeMode.RANDOM -> this
    PracticeMode.NEW -> filterNot { it.id in attemptedIds }
    PracticeMode.MISTAKES -> {
      val incorrectIds = attempts.asSequence()
        .filterNot { it.isCorrect }
        .map { it.questionId }
        .toSet()
      filter { it.id in incorrectIds }
    }
  }
}

internal fun filterPracticeCandidates(
  candidates: List<QuestionEntity>,
  questionTopics: List<QuestionTopicEntity>,
  config: PracticeSessionConfig,
): List<QuestionEntity> = candidates
  .filter { it.validateForPractice().valid }
  .applyQuestionBankFilters(config.toQuestionBankFilterState(), questionTopics)
