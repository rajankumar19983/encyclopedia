package com.rajankumar.encyclopaedia.feature.revision

import com.rajankumar.encyclopaedia.data.local.QuestionAttemptEntity
import com.rajankumar.encyclopaedia.data.local.QuestionEntity

fun buildRevisionQueue(questions: List<QuestionEntity>, attempts: List<QuestionAttemptEntity>): List<RevisionItem> {
  val histories = attempts.groupBy { it.questionId }
  return questions.mapNotNull { question ->
    val history = histories[question.id].orEmpty()
    val mistakes = history.count { !it.isCorrect }
    val reasons = history.revisionReasons()
    if (reasons.isEmpty()) null else RevisionItem(question, mistakes, history.size, revisionPriority(mistakes, history.size), reasons)
  }.sortedBy { it.priority.ordinal }
}
