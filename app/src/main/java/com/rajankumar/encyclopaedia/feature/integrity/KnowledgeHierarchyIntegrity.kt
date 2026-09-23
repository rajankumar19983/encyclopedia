package com.rajankumar.encyclopaedia.feature.integrity

import com.rajankumar.encyclopaedia.feature.backup.BackupSnapshot

fun BackupSnapshot.hasValidKnowledgeHierarchy(): Boolean {
  val ids = knowledgeNodes.map { it.id }.toSet()
  if (!allOptionalReferencesExist(knowledgeNodes.map { it.parentId }, ids)) return false
  return hasAcyclicParents(knowledgeNodes.associate { it.id to it.parentId })
}
