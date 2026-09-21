package com.rajankumar.encyclopaedia.feature.backup

const val BACKUP_FORMAT_VERSION = 2
const val MIN_SUPPORTED_BACKUP_FORMAT_VERSION = 1
const val MAX_RESTORE_POINTS = 5

enum class BackupType { AUTOMATIC, MANUAL }
enum class BackupDestination { DEVICE, GOOGLE_DRIVE }

data class BackupManifest(
  val formatVersion: Int = BACKUP_FORMAT_VERSION,
  val createdAt: Long,
  val backupType: BackupType = BackupType.MANUAL,
  val knowledgeNodeCount: Int,
  val lessonCount: Int,
  val questionCount: Int,
  val questionTopicCount: Int,
  val attemptCount: Int,
  val plannerTaskCount: Int,
  val notebookPageCount: Int = 0,
  val notebookLayerCount: Int = 0,
  val notebookStrokeCount: Int = 0
) {
  val totalRecords: Int get() = knowledgeNodeCount + lessonCount + questionCount + questionTopicCount + attemptCount + plannerTaskCount + notebookPageCount + notebookLayerCount + notebookStrokeCount
}

data class BackupRestorePoint(val name: String, val createdAt: Long, val type: BackupType, val destination: BackupDestination, val valid: Boolean = true)
