package com.rajankumar.encyclopaedia.feature.backup

fun BackupSnapshot.hasValidStudyRelationships(): Boolean {
  val nodeIds = knowledgeNodes.map { it.id }.toSet()
  val questionIds = questions.map { it.id }.toSet()
  if (knowledgeNodes.any { it.parentId != null && it.parentId !in nodeIds }) return false
  if (lessons.any { it.knowledgeNodeId !in nodeIds }) return false
  if (questionTopics.any { it.questionId !in questionIds || it.knowledgeNodeId !in nodeIds }) return false
  if (attempts.any { it.questionId !in questionIds }) return false
  return true
}

fun BackupSnapshot.hasValidNotebookRelationships(): Boolean {
  val nodeIds = knowledgeNodes.map { it.id }.toSet()
  val pageIds = notebookPages.map { it.id }.toSet()
  val layerIds = notebookLayers.map { it.id }.toSet()
  if (notebookPages.any { it.knowledgeNodeId != null && it.knowledgeNodeId !in nodeIds }) return false
  if (notebookLayers.any { it.pageId !in pageIds }) return false
  if (notebookStrokes.any { it.layerId !in layerIds }) return false
  return true
}

fun BackupSnapshot.isSafeToRestore(): Boolean =
  isInternallyConsistent() && hasValidStudyRelationships() && hasValidNotebookRelationships()
