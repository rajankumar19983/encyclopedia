package com.rajankumar.encyclopaedia.feature.backup

const val BACKUP_FORMAT_VERSION = 1

data class BackupManifest(
  val formatVersion: Int = BACKUP_FORMAT_VERSION,
  val createdAt: Long,
  val knowledgeNodeCount: Int,
  val lessonCount: Int,
  val questionCount: Int,
  val questionTopicCount: Int,
  val attemptCount: Int,
  val plannerTaskCount: Int
) {
  val totalRecords: Int
    get() = knowledgeNodeCount + lessonCount + questionCount + questionTopicCount + attemptCount + plannerTaskCount
}
