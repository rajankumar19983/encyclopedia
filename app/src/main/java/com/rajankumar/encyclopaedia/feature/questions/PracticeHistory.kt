package com.rajankumar.encyclopaedia.feature.questions

import com.rajankumar.encyclopaedia.data.local.QuestionAttemptEntity

data class PracticeHistorySummary(
  val attempts: Int,
  val correct: Int,
  val uniqueQuestions: Int
) {
  val accuracy: Int get() = accuracyPercent(correct, attempts)
}

fun List<QuestionAttemptEntity>.historySummary(): PracticeHistorySummary = PracticeHistorySummary(
  attempts = size,
  correct = count { it.isCorrect },
  uniqueQuestions = map { it.questionId }.distinct().size
)
