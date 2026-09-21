package com.rajankumar.encyclopaedia.feature.backup

/**
 * Runs the same validated rolling-backup policy for remote or local storage
 * implementations. This is used by Google Drive so Drive-specific transport
 * never owns backup validation or retention rules.
 */
class PortableBackupManager(
  private val storage: BackupStorage
) {
  suspend fun create(
    snapshot: BackupSnapshot,
    type: BackupType = BackupType.MANUAL
  ): BackupRestorePoint {
    require(snapshot.isInternallyConsistent()) { "Backup snapshot is internally inconsistent" }
    val effectiveSnapshot = snapshot.copy(
      manifest = snapshot.manifest.copy(backupType = type)
    )
    val name = backupFileName(
      createdAt = effectiveSnapshot.manifest.createdAt,
      type = type,
      formatVersion = effectiveSnapshot.manifest.formatVersion
    )
    val stored = storage.write(name, BackupCodec.encode(effectiveSnapshot))

    try {
      val verified = BackupCodec.decode(storage.read(stored.id))
      require(verified.manifest == effectiveSnapshot.manifest) { "Backup verification failed" }
    } catch (error: Throwable) {
      runCatching { storage.delete(stored.id) }
      throw error
    }

    pruneAfterSuccessfulBackup()
    return BackupRestorePoint(
      name = name,
      createdAt = effectiveSnapshot.manifest.createdAt,
      type = type,
      destination = storage.destination,
      valid = true
    )
  }

  suspend fun discoverRestorePoints(): List<BackupRestorePoint> = storage.discover().map { file ->
    val snapshot = runCatching { BackupCodec.decode(storage.read(file.id)) }.getOrNull()
    BackupRestorePoint(
      name = file.name,
      createdAt = snapshot?.manifest?.createdAt ?: file.lastModified,
      type = snapshot?.manifest?.backupType ?: typeFromName(file.name),
      destination = storage.destination,
      valid = snapshot != null
    )
  }.sortedByDescending(BackupRestorePoint::createdAt)

  suspend fun load(id: String): BackupSnapshot = BackupCodec.decode(storage.read(id))

  private suspend fun pruneAfterSuccessfulBackup() {
    val files = storage.discover()
    val byName = files.associateBy(BackupStoredObject::name)
    val points = files.map { file ->
      val snapshot = runCatching { BackupCodec.decode(storage.read(file.id)) }.getOrNull()
      BackupRestorePoint(
        name = file.name,
        createdAt = snapshot?.manifest?.createdAt ?: file.lastModified,
        type = snapshot?.manifest?.backupType ?: typeFromName(file.name),
        destination = storage.destination,
        valid = snapshot != null
      )
    }
    BackupRetention.removableAfterSuccessfulBackup(points).forEach { point ->
      byName[point.name]?.let { storage.delete(it.id) }
    }
  }

  private fun typeFromName(name: String): BackupType =
    if ("_automatic_" in name) BackupType.AUTOMATIC else BackupType.MANUAL
}
