package com.rajankumar.encyclopaedia.feature.revision

import com.rajankumar.encyclopaedia.data.local.QuestionAttemptEntity
import com.rajankumar.encyclopaedia.data.local.QuestionEntity

fun buildRevisionQueue(questions: List<QuestionEntity>, attempts: List<QuestionAttemptEntity>): List<RevisionItem> {
  val histories = attempts.groupBy { it.questionId }
  return questions.mapNotNull { question ->
    val history = histories[question.id].orEmpty().sortedBy { it.attemptedAt }
    if (history.isEmpty()) return@mapNotNull null

    val latest = history.last()
    val latestIncorrectIndex = history.indexOfLast { !it.isCorrect }
    val correctSinceLatestMistake = if (latestIncorrectIndex < 0) 0 else {
      history.drop(latestIncorrectIndex + 1).count { it.isCorrect }
    }

    // A question leaves the automatic revision queue after two consecutive
    // correct attempts following its most recent mistake. This prevents an
    // old incorrect answer from keeping an already-mastered question forever.
    if (latest.isCorrect && correctSinceLatestMistake >= 2) return@mapNotNull null

    val mistakes = history.count { !it.isCorrect }
    val reasons = history.revisionReasons()
    if (reasons.isEmpty()) null else {
      RevisionItem(
        question = question,
        mistakes = mistakes,
        attempts = history.size,
        priority = revisionPriority(mistakes, history.size),
        reasons = reasons
      )
    }
  }.sortedBy { it.priority.ordinal }
}
