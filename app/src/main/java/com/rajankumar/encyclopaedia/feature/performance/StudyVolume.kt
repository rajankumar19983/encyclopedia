package com.rajankumar.encyclopaedia.feature.performance

import com.rajankumar.encyclopaedia.data.local.QuestionAttemptEntity

data class StudyVolume(
  val attempts: Int,
  val correct: Int,
  val mistakes: Int,
  val totalTimeMs: Long,
) {
  val accuracyPercent: Int get() = if (attempts == 0) 0 else correct * 100 / attempts
}

fun List<QuestionAttemptEntity>.studyVolume(): StudyVolume = StudyVolume(
  attempts = size,
  correct = count { it.isCorrect },
  mistakes = count { !it.isCorrect },
  totalTimeMs = sumOf { it.timeTakenMs.coerceAtLeast(0) },
)
