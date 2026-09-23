package com.rajankumar.encyclopaedia.feature.integrity

enum class IntegritySeverity {
  ERROR,
  WARNING
}

fun IntegrityIssue.severity(): IntegritySeverity = when (this) {
  IntegrityIssue.MANIFEST,
  IntegrityIssue.IDS,
  IntegrityIssue.KNOWLEDGE_HIERARCHY,
  IntegrityIssue.STUDY_RELATIONSHIPS,
  IntegrityIssue.NOTEBOOK_RELATIONSHIPS -> IntegritySeverity.ERROR

  IntegrityIssue.FIELDS,
  IntegrityIssue.QUESTION_TOPICS -> IntegritySeverity.WARNING
}
