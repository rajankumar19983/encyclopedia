package com.rajankumar.encyclopaedia.feature.revision

import com.rajankumar.encyclopaedia.data.local.QuestionAttemptEntity

fun List<QuestionAttemptEntity>.revisionReasons(): Set<RevisionReason> = buildSet {
  val mistakes = count { !it.isCorrect }
  if (mistakes > 0) add(RevisionReason.INCORRECT)
  if (mistakes >= 2) add(RevisionReason.REPEATED_MISTAKE)
  if (any { it.timeTakenMs >= 60_000 }) add(RevisionReason.SLOW_ANSWER)
  if (isNotEmpty() && count { it.isCorrect } * 100 / size < 60) add(RevisionReason.LOW_ACCURACY)
}
