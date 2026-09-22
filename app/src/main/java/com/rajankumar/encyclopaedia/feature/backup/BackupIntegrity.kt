package com.rajankumar.encyclopaedia.feature.backup

private fun List<String>.allUnique(): Boolean = size == toSet().size

fun BackupSnapshot.hasUniquePrimaryIds(): Boolean =
  knowledgeNodes.map { it.id }.allUnique() &&
    lessons.map { it.id }.allUnique() &&
    questions.map { it.id }.allUnique() &&
    attempts.map { it.id }.allUnique() &&
    plannerTasks.map { it.id }.allUnique() &&
    notebookPages.map { it.id }.allUnique() &&
    notebookLayers.map { it.id }.allUnique() &&
    notebookStrokes.map { it.id }.allUnique() &&
    questionTopics.map { it.questionId to it.knowledgeNodeId }.let { it.size == it.toSet().size }

fun BackupSnapshot.hasAcyclicKnowledgeHierarchy(): Boolean {
  val parents = knowledgeNodes.associate { it.id to it.parentId }
  for (node in knowledgeNodes) {
    val visited = mutableSetOf<String>()
    var current: String? = node.id
    while (current != null) {
      if (!visited.add(current)) return false
      current = parents[current]
    }
  }
  return true
}

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
  isInternallyConsistent() && hasUniquePrimaryIds() && hasAcyclicKnowledgeHierarchy() && hasValidStudyRelationships() && hasValidNotebookRelationships()
