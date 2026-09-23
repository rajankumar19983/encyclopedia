package com.rajankumar.encyclopaedia.feature.integrity

fun IntegrityIssue.guidance(): String = when (this) {
  IntegrityIssue.MANIFEST -> "Do not restore this backup. Create a fresh backup from a healthy database or choose another restore point."
  IntegrityIssue.IDS -> "Avoid restoring until blank or duplicate identifiers are repaired, because records may overwrite or reference the wrong data."
  IntegrityIssue.FIELDS -> "Review recently created or imported content and repair missing or invalid required values."
  IntegrityIssue.KNOWLEDGE_HIERARCHY -> "Repair missing topic parents or hierarchy cycles before restoring or reorganizing topics."
  IntegrityIssue.QUESTION_TOPICS -> "Review question-topic assignments and remove blank or duplicate links."
  IntegrityIssue.STUDY_RELATIONSHIPS -> "Repair attempts or planner items that point to questions or topics that no longer exist."
  IntegrityIssue.NOTEBOOK_RELATIONSHIPS -> "Repair notebook pages, layers, strokes, or topic links that reference missing parent records."
}
