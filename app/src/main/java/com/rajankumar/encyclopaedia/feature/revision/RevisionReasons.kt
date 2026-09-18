package com.rajankumar.encyclopaedia.feature.revision

import com.rajankumar.encyclopaedia.data.local.QuestionAttemptEntity

private const val SLOW_ANSWER_THRESHOLD_MS = 60_000L
private const val LOW_ACCURACY_PERCENT = 60

fun List<QuestionAttemptEntity>.revisionReasons(): Set<RevisionReason> {
  if (isEmpty()) return emptySet()

  val attemptCount = size
  val mistakeCount = count { attempt -> !attempt.isCorrect }
  val correctCount = attemptCount - mistakeCount
  val hasSlowAnswer = any { attempt ->
    attempt.timeTakenMs >= SLOW_ANSWER_THRESHOLD_MS
  }

  return buildSet {
    if (mistakeCount > 0) add(RevisionReason.INCORRECT)
    if (mistakeCount >= 2) add(RevisionReason.REPEATED_MISTAKE)
    if (hasSlowAnswer) add(RevisionReason.SLOW_ANSWER)
    if (correctCount * 100 / attemptCount < LOW_ACCURACY_PERCENT) {
      add(RevisionReason.LOW_ACCURACY)
    }
  }
}
