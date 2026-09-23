package com.rajankumar.encyclopaedia.feature.integrity

import com.rajankumar.encyclopaedia.feature.backup.BackupSnapshot

data class IntegrityReport(
  val issues: Set<IntegrityIssue>,
  val recordCount: Int
) {
  val valid: Boolean get() = issues.isEmpty()
  val messages: List<String> get() = issues.map { it.message() }
}

fun BackupSnapshot.integrityReport(): IntegrityReport = IntegrityReport(
  issues = integrityIssues(),
  recordCount = knowledgeNodes.size + lessons.size + questions.size + questionTopics.size + attempts.size + plannerTasks.size + notebookPages.size + notebookLayers.size + notebookStrokes.size
)
