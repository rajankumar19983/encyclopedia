package com.rajankumar.encyclopaedia.feature.integrity

fun IntegrityIssue.message(): String = when (this) {
  IntegrityIssue.MANIFEST -> "Backup record counts or format metadata are inconsistent."
  IntegrityIssue.IDS -> "One or more data records have blank or duplicate identifiers."
  IntegrityIssue.FIELDS -> "One or more data records contain invalid required fields."
  IntegrityIssue.KNOWLEDGE_HIERARCHY -> "The topic hierarchy contains a missing parent or cycle."
  IntegrityIssue.QUESTION_TOPICS -> "Question-topic links contain blank or duplicate pairs."
  IntegrityIssue.STUDY_RELATIONSHIPS -> "Study records reference missing questions or topics."
  IntegrityIssue.NOTEBOOK_RELATIONSHIPS -> "Notebook records reference missing pages, layers, or topics."
}
