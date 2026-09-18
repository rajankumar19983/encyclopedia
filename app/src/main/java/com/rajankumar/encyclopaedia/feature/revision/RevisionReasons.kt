package com.rajankumar.encyclopaedia.feature.revision

import com.rajankumar.encyclopaedia.data.local.QuestionAttemptEntity

fun List<QuestionAttemptEntity>.revisionReasons(): Set<RevisionReason> {
  if (isEmpty()) return emptySet()

  var mistakes = 0
  var correct = 0
  var hasSlowAnswer = false

  for (attempt in this) {
    if (attempt.isCorrect) {
      correct++
    } else {
      mistakes++
    }

    if (attempt.timeTakenMs >= 60_000) {
      hasSlowAnswer = true
    }
  }

  return buildSet {
    if (mistakes > 0) add(RevisionReason.INCORRECT)
    if (mistakes >= 2) add(RevisionReason.REPEATED_MISTAKE)
    if (hasSlowAnswer) add(RevisionReason.SLOW_ANSWER)
    if (correct * 100 / size < 60) add(RevisionReason.LOW_ACCURACY)
  }
}
