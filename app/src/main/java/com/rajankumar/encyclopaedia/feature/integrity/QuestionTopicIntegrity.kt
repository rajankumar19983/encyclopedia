package com.rajankumar.encyclopaedia.feature.integrity

import com.rajankumar.encyclopaedia.feature.backup.BackupSnapshot

fun BackupSnapshot.hasUniqueQuestionTopics(): Boolean {
  val pairs = questionTopics.map { it.questionId to it.knowledgeNodeId }
  return pairs.size == pairs.toSet().size && pairs.all { (questionId, nodeId) -> questionId.isNotBlank() && nodeId.isNotBlank() }
}
