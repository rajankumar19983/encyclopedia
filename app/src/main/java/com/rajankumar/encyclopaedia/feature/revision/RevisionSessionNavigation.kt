package com.rajankumar.encyclopaedia.feature.revision

fun RevisionSessionState.nextQuestionId(): Long? =
  questionIds.firstOrNull { it !in completedQuestionIds }

fun RevisionSessionState.contains(questionId: Long): Boolean = questionId in questionIds

fun RevisionSessionState.isCompleted(questionId: Long): Boolean = questionId in completedQuestionIds
