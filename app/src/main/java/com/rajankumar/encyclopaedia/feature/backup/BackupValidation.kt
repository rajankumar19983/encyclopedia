package com.rajankumar.encyclopaedia.feature.backup

sealed interface BackupValidationResult {
  data object Valid : BackupValidationResult
  data class Invalid(val reason: String) : BackupValidationResult
}

fun validateBackupSnapshot(snapshot: BackupSnapshot): BackupValidationResult {
  if (snapshot.manifest.formatVersion != BACKUP_FORMAT_VERSION) {
    return BackupValidationResult.Invalid(
      "Unsupported backup format ${snapshot.manifest.formatVersion}. Expected $BACKUP_FORMAT_VERSION."
    )
  }
  if (!snapshot.isInternallyConsistent()) {
    return BackupValidationResult.Invalid("Backup record counts do not match its manifest.")
  }

  val nodeIds = snapshot.knowledgeNodes.map { it.id }.toSet()
  if (snapshot.knowledgeNodes.any { it.parentId != null && it.parentId !in nodeIds }) {
    return BackupValidationResult.Invalid("Backup contains a knowledge node with a missing parent.")
  }
  if (snapshot.lessons.any { it.knowledgeNodeId !in nodeIds }) {
    return BackupValidationResult.Invalid("Backup contains a lesson with a missing knowledge node.")
  }

  val questionIds = snapshot.questions.map { it.id }.toSet()
  if (snapshot.questionTopics.any { it.questionId !in questionIds || it.knowledgeNodeId !in nodeIds }) {
    return BackupValidationResult.Invalid("Backup contains an invalid question-topic relationship.")
  }
  if (snapshot.attempts.any { it.questionId !in questionIds }) {
    return BackupValidationResult.Invalid("Backup contains an attempt for a missing question.")
  }

  return BackupValidationResult.Valid
}
