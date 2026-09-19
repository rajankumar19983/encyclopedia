package com.rajankumar.encyclopaedia.feature.revision

data class RevisionSessionState(
  val questionIds: List<String>,
  val completedQuestionIds: Set<String> = emptySet()
) {
  val progress: RevisionProgress
    get() = RevisionProgress(completedQuestionIds.count { it in questionIds }, questionIds.size)

  val isComplete: Boolean
    get() = questionIds.isNotEmpty() && progress.completed >= progress.total

  fun markCompleted(questionId: String): RevisionSessionState =
    if (questionId !in questionIds) this
    else copy(completedQuestionIds = completedQuestionIds + questionId)
}

fun RevisionSession.state() = RevisionSessionState(questions.map { it.id })
