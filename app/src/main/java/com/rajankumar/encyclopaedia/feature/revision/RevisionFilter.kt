package com.rajankumar.encyclopaedia.feature.revision

fun List<RevisionItem>.filterRevisionQueue(
  priority: RevisionPriority? = null,
  query: String = ""
): List<RevisionItem> {
  val normalizedQuery = query.trim()
  return filter { item ->
    val matchesPriority = priority == null || item.priority == priority
    val matchesQuery = normalizedQuery.isBlank() ||
      item.question.questionText.contains(normalizedQuery, ignoreCase = true) ||
      item.reasons.any { it.label.contains(normalizedQuery, ignoreCase = true) }
    matchesPriority && matchesQuery
  }
}
