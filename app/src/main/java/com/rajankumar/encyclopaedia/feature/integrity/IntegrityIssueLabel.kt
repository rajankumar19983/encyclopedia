package com.rajankumar.encyclopaedia.feature.integrity

fun IntegrityIssue.label(): String = when (this) {
  IntegrityIssue.MANIFEST -> "Backup metadata"
  IntegrityIssue.IDS -> "Record identifiers"
  IntegrityIssue.FIELDS -> "Required fields"
  IntegrityIssue.KNOWLEDGE_HIERARCHY -> "Knowledge hierarchy"
  IntegrityIssue.QUESTION_TOPICS -> "Question-topic links"
  IntegrityIssue.STUDY_RELATIONSHIPS -> "Study relationships"
  IntegrityIssue.NOTEBOOK_RELATIONSHIPS -> "Notebook relationships"
}
