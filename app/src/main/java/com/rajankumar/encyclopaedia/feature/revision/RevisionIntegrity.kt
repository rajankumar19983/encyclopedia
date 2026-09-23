package com.rajankumar.encyclopaedia.feature.revision

data class RevisionIntegritySummary(
  val dueQuestionIds: Set<String>,
  val topicDueQuestionIds: Set<String>,
  val untaggedDueQuestionIds: Set<String>,
) {
  val taggedDueQuestionsAreConsistent: Boolean
    get() = topicDueQuestionIds.all { it in dueQuestionIds }
}

fun revisionIntegritySummary(
  queue: List<RevisionItem>,
  topicStates: List<TopicRevisionState>,
): RevisionIntegritySummary {
  val due = queue.map { it.question.id }.toSet()
  val topicDue = topicStates.flatMap { it.revisionQuestionIds }.toSet()
  return RevisionIntegritySummary(
    dueQuestionIds = due,
    topicDueQuestionIds = topicDue,
    untaggedDueQuestionIds = due - topicDue,
  )
}
