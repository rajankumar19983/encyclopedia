package com.rajankumar.encyclopaedia.feature.backup

/**
 * Destination-neutral contract used by the backup pipeline. Device storage and
 * Google Drive can implement the same validation/retention behavior without
 * duplicating backup logic.
 */
interface BackupStorage {
  val destination: BackupDestination

  suspend fun write(name: String, bytes: ByteArray): BackupStoredObject

  suspend fun read(id: String): ByteArray

  suspend fun discover(): List<BackupStoredObject>

  suspend fun delete(id: String): Boolean
}

data class BackupStoredObject(
  val id: String,
  val name: String,
  val lastModified: Long,
  val sizeBytes: Long
)
