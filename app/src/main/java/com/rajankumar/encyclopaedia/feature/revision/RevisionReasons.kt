package com.rajankumar.encyclopaedia.feature.revision

import com.rajankumar.encyclopaedia.data.local.QuestionAttemptEntity

fun List<QuestionAttemptEntity>.revisionReasons(): Set<RevisionReason> {
  val attempts = this

  return buildSet {
    val mistakes = attempts.count { !it.isCorrect }
    if (mistakes > 0) add(RevisionReason.INCORRECT)
    if (mistakes >= 2) add(RevisionReason.REPEATED_MISTAKE)
    if (attempts.any { it.timeTakenMs >= 60_000 }) add(RevisionReason.SLOW_ANSWER)
    if (attempts.isNotEmpty() && attempts.count { it.isCorrect } * 100 / attempts.size < 60) {
      add(RevisionReason.LOW_ACCURACY)
    }
  }
}
