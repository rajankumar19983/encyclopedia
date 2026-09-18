package com.rajankumar.encyclopaedia.feature.questions

import com.rajankumar.encyclopaedia.data.local.QuestionAttemptEntity

data class PracticeAnalytics(
  val attempts: Int,
  val correct: Int,
  val practisedQuestions: Int,
  val averageTimeMs: Long
) {
  val accuracy: Int get() = accuracyPercent(correct, attempts)
}

fun List<QuestionAttemptEntity>.toPracticeAnalytics(): PracticeAnalytics = PracticeAnalytics(
  attempts = size,
  correct = count { it.isCorrect },
  practisedQuestions = map { it.questionId }.distinct().size,
  averageTimeMs = if (isEmpty()) 0 else sumOf { it.timeTakenMs.coerceAtLeast(0) } / size
)
