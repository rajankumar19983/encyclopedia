package com.rajankumar.encyclopaedia.feature.backup

private fun List<String>.allUnique(): Boolean = size == toSet().size
private fun List<String>.allPresent(): Boolean = all { it.isNotBlank() }

fun BackupSnapshot.hasValidPrimaryIds(): Boolean {
  val idGroups = listOf(
    knowledgeNodes.map { it.id }, lessons.map { it.id }, questions.map { it.id }, attempts.map { it.id },
    plannerTasks.map { it.id }, notebookPages.map { it.id }, notebookLayers.map { it.id }, notebookStrokes.map { it.id }
  )
  return idGroups.all { it.allPresent() && it.allUnique() } &&
    questionTopics.all { it.questionId.isNotBlank() && it.knowledgeNodeId.isNotBlank() } &&
    questionTopics.map { it.questionId to it.knowledgeNodeId }.let { it.size == it.toSet().size }
}

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
  isInternallyConsistent() && hasValidPrimaryIds() && hasAcyclicKnowledgeHierarchy() && hasValidStudyRelationships() && hasValidNotebookRelationships()
