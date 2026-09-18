package com.rajankumar.encyclopaedia.feature.revision

fun List<RevisionItem>.filterRevisionQueue(priority: RevisionPriority? = null, query: String = ""): List<RevisionItem> = filter {
  (priority == null || it.priority == priority) &&
    (query.isBlank() || it.question.questionText.contains(query.trim(), ignoreCase = true))
}
