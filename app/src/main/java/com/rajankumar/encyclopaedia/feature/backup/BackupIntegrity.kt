package com.rajankumar.encyclopaedia.feature.backup

fun BackupSnapshot.hasValidStudyRelationships(): Boolean {
  val nodeIds = knowledgeNodes.map { it.id }.toSet()
  val questionIds = questions.map { it.id }.toSet()
  if (lessons.any { it.knowledgeNodeId !in nodeIds }) return false
  if (questionTopics.any { it.questionId !in questionIds || it.knowledgeNodeId !in nodeIds }) return false
  if (attempts.any { it.questionId !in questionIds }) return false
  return true
}

fun BackupSnapshot.isSafeToRestore(): Boolean =
  isInternallyConsistent() && hasValidStudyRelationships()
