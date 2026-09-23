package com.rajankumar.encyclopaedia.feature.integrity

import com.rajankumar.encyclopaedia.feature.backup.BackupSnapshot

fun BackupSnapshot.hasValidStudyRelationships(): Boolean {
  val nodeIds = knowledgeNodes.map { it.id }.toSet()
  val questionIds = questions.map { it.id }.toSet()
  return allReferencesExist(lessons.map { it.knowledgeNodeId }, nodeIds) &&
    allReferencesExist(questionTopics.map { it.questionId }, questionIds) &&
    allReferencesExist(questionTopics.map { it.knowledgeNodeId }, nodeIds) &&
    allReferencesExist(attempts.map { it.questionId }, questionIds)
}
