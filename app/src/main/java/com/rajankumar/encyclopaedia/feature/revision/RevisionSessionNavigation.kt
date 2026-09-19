package com.rajankumar.encyclopaedia.feature.revision

fun RevisionSessionState.nextQuestionId(): String? =
  questionIds.firstOrNull { it !in completedQuestionIds }

fun RevisionSessionState.contains(questionId: String): Boolean = questionId in questionIds

fun RevisionSessionState.isCompleted(questionId: String): Boolean = questionId in completedQuestionIds
