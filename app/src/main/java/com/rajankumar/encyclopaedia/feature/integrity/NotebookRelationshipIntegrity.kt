package com.rajankumar.encyclopaedia.feature.integrity

import com.rajankumar.encyclopaedia.feature.backup.BackupSnapshot

fun BackupSnapshot.hasValidNotebookRelationships(): Boolean {
  val nodeIds = knowledgeNodes.map { it.id }.toSet()
  val pageIds = notebookPages.map { it.id }.toSet()
  val layerIds = notebookLayers.map { it.id }.toSet()
  return allOptionalReferencesExist(notebookPages.map { it.knowledgeNodeId }, nodeIds) &&
    allReferencesExist(notebookLayers.map { it.pageId }, pageIds) &&
    allReferencesExist(notebookStrokes.map { it.layerId }, layerIds)
}
