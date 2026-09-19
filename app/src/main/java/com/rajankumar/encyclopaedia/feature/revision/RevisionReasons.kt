package com.rajankumar.encyclopaedia.feature.revision

import com.rajankumar.encyclopaedia.data.local.QuestionAttemptEntity

private const val LOW_ACCURACY_PERCENT = 60

fun List<QuestionAttemptEntity>.revisionReasons(): Set<RevisionReason> {
  if (isEmpty()) return emptySet()

  val attemptCount = size
  val mistakeCount = count { attempt -> !attempt.isCorrect }
  val correctCount = attemptCount - mistakeCount
  val hasSlowAnswer = any { attempt ->
    attempt.timeTakenMs >= RevisionConstants.slowAnswerMs
  }

  return buildSet {
    if (mistakeCount > 0) add(RevisionReason.INCORRECT)
    if (mistakeCount >= RevisionConstants.repeatedMistakeCount) add(RevisionReason.REPEATED_MISTAKE)
    if (hasSlowAnswer) add(RevisionReason.SLOW_ANSWER)
    if (correctCount * 100 / attemptCount < LOW_ACCURACY_PERCENT) {
      add(RevisionReason.LOW_ACCURACY)
    }
  }
}
