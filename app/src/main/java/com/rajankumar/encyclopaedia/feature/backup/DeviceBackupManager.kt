package com.rajankumar.encyclopaedia.feature.backup

import android.net.Uri

/**
 * Coordinates portable device backups. A newly written backup is decoded and
 * validated before older restore points become eligible for removal.
 */
class DeviceBackupManager(
  private val store: DeviceBackupStore
) {
  fun create(
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
    val uri = store.write(name, BackupCodec.encode(effectiveSnapshot))

    try {
      val verified = BackupCodec.decode(store.read(uri))
      require(verified.manifest == effectiveSnapshot.manifest) {
        "Backup verification failed"
      }
    } catch (error: Throwable) {
      store.delete(uri)
      throw error
    }

    pruneAfterSuccessfulBackup()
    return BackupRestorePoint(
      name = name,
      createdAt = effectiveSnapshot.manifest.createdAt,
      type = type,
      destination = BackupDestination.DEVICE,
      valid = true
    )
  }

  fun discoverRestorePoints(): List<BackupRestorePoint> = store.discover().map { file ->
    val decoded = runCatching { BackupCodec.decode(store.read(file.uri)) }.getOrNull()
    BackupRestorePoint(
      name = file.name,
      createdAt = decoded?.manifest?.createdAt ?: file.lastModified,
      type = decoded?.manifest?.backupType ?: typeFromName(file.name),
      destination = BackupDestination.DEVICE,
      valid = decoded != null
    )
  }.sortedByDescending(BackupRestorePoint::createdAt)

  fun load(uri: Uri): BackupSnapshot = BackupCodec.decode(store.read(uri))

  private fun pruneAfterSuccessfulBackup() {
    val files = store.discover()
    val byName = files.associateBy(StoredBackupFile::name)
    val points = files.map { file ->
      val snapshot = runCatching { BackupCodec.decode(store.read(file.uri)) }.getOrNull()
      BackupRestorePoint(
        name = file.name,
        createdAt = snapshot?.manifest?.createdAt ?: file.lastModified,
        type = snapshot?.manifest?.backupType ?: typeFromName(file.name),
        destination = BackupDestination.DEVICE,
        valid = snapshot != null
      )
    }
    BackupRetention.removableAfterSuccessfulBackup(points).forEach { point ->
      byName[point.name]?.let { store.delete(it.uri) }
    }
  }

  private fun typeFromName(name: String): BackupType =
    if ("_automatic_" in name) BackupType.AUTOMATIC else BackupType.MANUAL
}
