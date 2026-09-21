package com.rajankumar.encyclopaedia.feature.backup

/**
 * Drive-facing boundary. Authentication and the concrete Google Drive client
 * remain outside the backup domain so they can change independently of the
 * portable backup format.
 */
interface GoogleDriveBackupClient {
  suspend fun uploadBackup(name: String, bytes: ByteArray): BackupStoredObject
  suspend fun downloadBackup(fileId: String): ByteArray
  suspend fun listBackups(): List<BackupStoredObject>
  suspend fun deleteBackup(fileId: String): Boolean
}

class GoogleDriveBackupStorage(
  private val client: GoogleDriveBackupClient
) : BackupStorage {
  override val destination: BackupDestination = BackupDestination.GOOGLE_DRIVE

  override suspend fun write(name: String, bytes: ByteArray): BackupStoredObject {
    require(isSupportedBackupFileName(name)) { "Unsupported Encyclopaedia backup filename" }
    return client.uploadBackup(name, bytes)
  }

  override suspend fun read(id: String): ByteArray = client.downloadBackup(id)

  override suspend fun discover(): List<BackupStoredObject> = client.listBackups()
    .filter { isSupportedBackupFileName(it.name) }
    .sortedByDescending(BackupStoredObject::lastModified)

  override suspend fun delete(id: String): Boolean = client.deleteBackup(id)
}
