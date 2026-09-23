package com.rajankumar.encyclopaedia.feature.integrity

import com.rajankumar.encyclopaedia.feature.backup.*

fun BackupSnapshot.integrityIssues(): Set<IntegrityIssue> = buildSet {
  if (!isSafeToRestore()) add(IntegrityIssue.MANIFEST)
  if (!hasValidDomainIds()) add(IntegrityIssue.IDS)
  if (!hasValidDomainFields()) add(IntegrityIssue.FIELDS)
  if (!hasValidKnowledgeHierarchy()) add(IntegrityIssue.KNOWLEDGE_HIERARCHY)
  if (!hasUniqueQuestionTopics()) add(IntegrityIssue.QUESTION_TOPICS)
  if (!hasValidStudyRelationships()) add(IntegrityIssue.STUDY_RELATIONSHIPS)
  if (!hasValidNotebookRelationships()) add(IntegrityIssue.NOTEBOOK_RELATIONSHIPS)
}
