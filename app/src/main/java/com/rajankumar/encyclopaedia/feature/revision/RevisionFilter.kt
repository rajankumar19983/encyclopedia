package com.rajankumar.encyclopaedia.feature.revision

fun normalizeRevisionQuery(query: String): String = query.trim().replace(Regex("\\s+"), " ").take(120)

fun List<RevisionItem>.filterRevisionQueue(
  priority: RevisionPriority? = null,
  query: String = ""
): List<RevisionItem> {
  val normalizedQuery = normalizeRevisionQuery(query)
  return filter { item ->
    val matchesPriority = priority == null || item.priority == priority
    val matchesQuery = normalizedQuery.isBlank() ||
      item.question.questionText.contains(normalizedQuery, ignoreCase = true) ||
      item.reasons.any { it.label.contains(normalizedQuery, ignoreCase = true) }
    matchesPriority && matchesQuery
  }
}
