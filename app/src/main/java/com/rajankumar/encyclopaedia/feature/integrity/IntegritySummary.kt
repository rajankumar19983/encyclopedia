package com.rajankumar.encyclopaedia.feature.integrity

import com.rajankumar.encyclopaedia.feature.backup.BackupSnapshot

data class IntegritySummary(val valid: Boolean, val recordCount: Int)

fun BackupSnapshot.integritySummary(): IntegritySummary = IntegritySummary(
  valid = passesRestoreIntegrityGate(),
  recordCount = knowledgeNodes.size + lessons.size + questions.size + questionTopics.size + attempts.size + plannerTasks.size + notebookPages.size + notebookLayers.size + notebookStrokes.size
)
